package com.muamuathu.feature.ads

import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.muamuathu.feature.TestDevice

object AdInitializer {
    fun init(context: Context) {
        MobileAds.initialize(context) {
            val testDeviceIds = TestDevice.all().map { it.id }
            val configuration = RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
            MobileAds.setRequestConfiguration(configuration)
        }
    }
}