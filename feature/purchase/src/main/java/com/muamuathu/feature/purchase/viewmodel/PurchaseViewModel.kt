package com.muamuathu.feature.purchase.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.muamuathu.feature.purchase.Billing
import com.muamuathu.feature.purchase.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(private val billing: Billing) : ViewModel() {

    fun restorePurchase() = billing.restorePurchase()

    fun buy(activity: Activity, productId: String, offerToken: String? = null): Boolean {
        return billing.launchBillingFlow(activity, productId, offerToken)
    }

    fun isPremiumUser(): Flow<Boolean> {
        val monthly = billing.isPurchased(BuildConfig.MonthlySubscription)
        val yearly = billing.isPurchased(BuildConfig.YearlySubscription)
        val pro = billing.isPurchased(BuildConfig.InAppItems)
        return monthly
            .combine(yearly) { m, y -> m || y }
            .combine(pro) { subscription, oneTime ->
                subscription || oneTime
            }
    }

    fun isPurchased(productId: String) = billing.isPurchased(productId)

    fun unsubscribe(activity: Activity, sku: String) {
        billing.unsubscribe(activity, sku)
    }

    fun getAllProducts() = billing.getAllProducts().map { it.values.toList() }

    fun getProduct(productId: String) = billing.getProduct(productId)
}