package com.muamuathu.feature.purchase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.ProductType
import com.muamuathu.feature.purchase.di.BillingLicenseKey
import com.muamuathu.feature.purchase.di.PurchaseCoroutineScope
import com.muamuathu.feature.purchase.di.PurchaseItems
import com.muamuathu.feature.purchase.ext.*
import com.muamuathu.feature.purchase.model.PurchaseItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Billing @Inject constructor(
    @ApplicationContext private val appContext: Context,
    @BillingLicenseKey private val licenseKey: String,
    @PurchaseItems private val purchaseItems: List<PurchaseItem>,
    @PurchaseCoroutineScope private val coroutineScope: CoroutineScope
) {

    private val Context.dataStore by preferencesDataStore(name = "app_purchase.ds")

    lateinit var billingClient: BillingClient
    private val productDetailsMap = MutableStateFlow<Map<String, ProductDetails>>(emptyMap())

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        billingResult.printLog()
        if (purchases.isNullOrEmpty()) return@PurchasesUpdatedListener
        if (billingResult.isSuccess()) {
            purchases.onEach { handlePurchase(it) }
        } else if (billingResult.isUserCancelled()) {
            purchases.onEach { cancelPurchase(it) }
        }
    }

    private fun cancelPurchase(purchase: Purchase) {
        val productMap = productDetailsMap.value
        val productDetails = productMap[purchase.products.first()] ?: return
        savePurchaseStatus(productDetails.productId, false)
    }

    private fun savePurchaseStatus(productId: String, purchased: Boolean) {
        coroutineScope.launch {
            appContext.dataStore.edit { settings ->
                val prefKey = booleanPreferencesKey(productId)
                settings[prefKey] = purchased
            }
        }
    }

    private fun connectBillingService() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                billingResult.printLog()
                coroutineScope.launch {
                    queryPurchaseDetails()
                    fetchPurchases()
                }
            }

            override fun onBillingServiceDisconnected() {
                coroutineScope.launch {
                    delay(5_000)
                    connectBillingService()
                }
            }
        })
    }

    private fun queryPurchaseDetails() {
        val productMap = purchaseItems.groupBy { it.type }

        val deferredProductDetails = productMap.map {
            val productList = it.value.map { purchaseItem ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(purchaseItem.id)
                    .setProductType(purchaseItem.type)
                    .build()
            }
            val params = QueryProductDetailsParams.newBuilder()
            params.setProductList(productList)

            coroutineScope.async {
                billingClient.queryProductDetails(params.build())
            }
        }

        coroutineScope.launch {
            productDetailsMap.value = deferredProductDetails.awaitAll()
                .filter { it.billingResult.isSuccess() }
                .mapNotNull { it.productDetailsList }
                .flatten()
                .associateBy { it.productId }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        when {
            purchase.isPurchased() -> processPurchase(purchase)
            purchase.isPending() -> processPendingPurchase(purchase)
        }
    }

    private fun processPurchase(purchase: Purchase) {
        val productMap = productDetailsMap.value
        val productDetails = productMap[purchase.products.first()] ?: return
        // 1. Verify
        if (purchase.isPurchased() && licenseKey.verifySignature(purchase)) {

            // 2. Grant entitlement to the user.
            if (purchase.products.isNotEmpty()) {
                savePurchaseStatus(productDetails.productId, true)
            }

            // 3. Consume or Ack
            when {
                purchase.isConsumable() -> {
                    val consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                    coroutineScope.launch {
                        val consumeResult = withContext(Dispatchers.IO) {
                            billingClient.consumePurchase(consumeParams)
                        }
                        consumeResult.billingResult.printLog()
                    }
                }
                !purchase.isAcknowledged -> {
                    val ackParams = AcknowledgePurchaseParams
                        .newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)

                    coroutineScope.launch {
                        val ackResult = withContext(Dispatchers.IO) {
                            billingClient.acknowledgePurchase(ackParams.build())
                        }
                        ackResult.printLog()
                    }
                }
            }
        } else {
            coroutineScope.launch {
                savePurchaseStatus(productDetails.productId, false)
            }
        }
    }

    private fun processPendingPurchase(purchase: Purchase) {
        Timber.tag("nt.dung").e("Purchase is in pending state!! ${purchase.products}")
    }

    private fun fetchPurchases() {
        val inAppParams = QueryPurchasesParams.newBuilder()
            .setProductType(ProductType.INAPP)
            .build()

        val subParams = QueryPurchasesParams.newBuilder()
            .setProductType(ProductType.SUBS)
            .build()

        coroutineScope.launch {
            val inAppResult = withContext(Dispatchers.IO) {
                billingClient.queryPurchasesAsync(params = inAppParams)
            }

            val subscriptionResult = withContext(Dispatchers.IO) {
                billingClient.queryPurchasesAsync(params = subParams)
            }

            listOf(inAppResult, subscriptionResult)
                .filter { it.billingResult.isSuccess() }
                .map { it.purchasesList }
                .flatten()
                .onEach {
                    handlePurchase(it)
                }
        }
    }

    fun init() {
        billingClient = BillingClient.newBuilder(appContext)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        connectBillingService()
    }

    fun getAllProducts() = productDetailsMap

    fun getProduct(productId: String) = productDetailsMap.map { it[productId] }

    fun restorePurchase() {
        fetchPurchases()
    }

    fun launchBillingFlow(activity: Activity, productId: String, offerToken: String? = null): Boolean {
        val productMap = productDetailsMap.value
        val productDetails = productMap[productId] ?: return false
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .apply { offerToken?.let { setOfferToken(it) } }
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)
        return billingResult.isSuccess()
    }

    fun isPurchased(purchaseItem: PurchaseItem) = isPurchased(purchaseItem.id)

    fun isPurchased(productId: String): Flow<Boolean> {
        val prefKey = booleanPreferencesKey(productId)
        return appContext.dataStore.data.map { it[prefKey] ?: false }
    }

    fun unsubscribe(activity: Activity, sku: String) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val subscriptionUrl = ("http://play.google.com/store/account/subscriptions"
                    + "?package=" + activity.packageName
                    + "&sku=" + sku)
            intent.data = Uri.parse(subscriptionUrl)
            activity.startActivity(intent)
            activity.finish()
        } catch (e: Exception) {
            Timber.tag("nt.dung").e(e)
        }
    }
}