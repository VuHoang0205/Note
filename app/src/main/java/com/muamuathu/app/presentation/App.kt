package com.muamuathu.app.presentation

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.muamuathu.app.BuildConfig
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
        AndroidThreeTen.init(this)
        setupLog()
    }

    private fun setupLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}