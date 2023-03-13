package com.muamuathu.feature.purchase.di

import com.android.billingclient.api.BillingClient
import com.muamuathu.feature.purchase.BuildConfig
import com.muamuathu.feature.purchase.model.PurchaseItem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PurchaseModule {

    @BillingLicenseKey
    @Provides
    @Singleton
    fun provideBillingLicenseKey(): String = BuildConfig.LicenseKey

    @PurchaseCoroutineScope
    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @PurchaseItems
    @Provides
    @Singleton
    fun providePurchaseList(): List<PurchaseItem> {
        return listOf(
            PurchaseItem(BuildConfig.MonthlySubscription, BillingClient.ProductType.SUBS),
            PurchaseItem(BuildConfig.InAppItems, BillingClient.ProductType.INAPP)
        )
    }
}