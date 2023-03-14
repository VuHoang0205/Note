package com.muamuathu.feature.interstitial

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.muamuathu.common.BuildConfig
import com.muamuathu.common.ConnectionState
import com.muamuathu.common.connectivityState
import com.muamuathu.feature.AdRequestFactory
import com.muamuathu.feature.AdUnit
import timber.log.Timber

@Composable
fun JcAdInterstitial(
    adUnit: AdUnit = if (BuildConfig.DEBUG) AdUnit.TestInterstitial else AdUnit.Interstitial,
    show: Boolean,
    onAdFailedToLoad: () -> Unit,
    onAdFailedToShow: () -> Unit,
    onAdClosed: () -> Unit
) {
    val activity = LocalContext.current as ComponentActivity
    var ad: InterstitialAd? by remember { mutableStateOf(null) }
    val connectionState by connectivityState()

    val fullContentCallback = remember {
        object : FullScreenContentCallback() {
            override fun onAdClicked() {
                super.onAdClicked()
                onAdClosed()
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                ad = null
                onAdClosed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                super.onAdFailedToShowFullScreenContent(error)
                Timber.tag("nt.dung").e(error.message)
                ad = null
                onAdFailedToShow()
            }
        }
    }

    DisposableEffect(show, connectionState) {
        if (connectionState == ConnectionState.Available) {
            InterstitialAd.load(
                activity,
                adUnit.unitId,
                AdRequestFactory.create(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Timber.tag("nt.dung").e(error.message)
                        ad = null
                        onAdFailedToLoad()
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Timber.tag("nt.dung").e("Ad loaded!!!")
                        ad = interstitialAd
                        ad?.fullScreenContentCallback = fullContentCallback
                    }
                })
        }

        onDispose {
            ad = null
        }
    }

    LaunchedEffect(ad, show) {
        if (show) {
            ad?.show(activity)
        }
    }
}