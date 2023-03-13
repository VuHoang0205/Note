package com.muamuathu.feature.purchase.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BillingLicenseKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PurchaseItems

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PurchaseCoroutineScope