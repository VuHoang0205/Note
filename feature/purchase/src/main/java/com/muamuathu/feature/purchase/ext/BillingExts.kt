package com.muamuathu.feature.purchase.ext

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState
import com.muamuathu.feature.purchase.ext.Security.verifyPurchase
import timber.log.Timber

fun BillingResult.printLog() {
    if (isSuccess()) {
        Timber.tag("nt.dung").d("SUCCESS!!")
    } else {
        Timber.tag("nt.dung").e("Billing Result failed: $responseCode - $debugMessage")
    }
}

fun BillingResult.isSuccess() = responseCode == BillingResponseCode.OK

fun BillingResult.isUserCancelled() = responseCode == BillingResponseCode.USER_CANCELED

fun Purchase.isPurchased() = purchaseState == PurchaseState.PURCHASED

fun Purchase.isPending() = purchaseState == PurchaseState.PENDING

fun Purchase.isConsumable() = false

fun String.verifySignature(purchase: Purchase): Boolean {
    return verifyPurchase(purchase.originalJson, purchase.signature, this)
}

