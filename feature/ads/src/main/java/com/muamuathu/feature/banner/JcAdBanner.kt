package com.muamuathu.feature.banner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.muamuathu.common.BuildConfig
import com.muamuathu.common.ConnectionState
import com.muamuathu.common.connectivityState
import com.muamuathu.feature.AdRequestFactory
import com.muamuathu.feature.AdUnit
import timber.log.Timber

@Composable
fun JcAdBanner(
    modifier: Modifier,
    ad: AdUnit = if (BuildConfig.DEBUG) AdUnit.TestBanner else AdUnit.Banner,
    onAdFailedToLoad: () -> Unit
) {
    val connectionState by connectivityState()
    var adView: AdView? = remember { null }
    if (connectionState == ConnectionState.Available) {
        Column(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.surface
            )
        ) {
            Divider(
                modifier = modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )

            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = {
                    adView = AdView(it)
                        .apply {
                            setAdSize(AdSize.BANNER)
                            adUnitId = ad.unitId
                        }.also {
                            it.adListener = object : AdListener() {
                                override fun onAdFailedToLoad(error: LoadAdError) {
                                    Timber.tag("nt.dung").e(error.message)
                                    onAdFailedToLoad()
                                }
                            }
                            it.loadAd(AdRequestFactory.create())
                        }
                    adView!!
                }
            )
        }


        DisposableEffect(ad) {
            onDispose {
                adView?.destroy()
            }
        }
    }
}