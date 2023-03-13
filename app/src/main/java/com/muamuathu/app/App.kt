package com.muamuathu.app

import android.app.Application
import com.muamuathu.feature.purchase.Billing
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var billing: Billing

    override fun onCreate() {
        super.onCreate()
        setupLog()
    }

    private fun setupLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}