package com.muamuathu.feature.native

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.muamuathu.feature.AdRequestFactory
import com.muamuathu.feature.AdUnit
import timber.log.Timber

@Composable
fun JcAdNative(modifier: Modifier, adUnit: AdUnit, onAdFailedToLoad: () -> Unit) {
    val context = LocalContext.current

    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    val adLoader = remember {
        AdLoader.Builder(context, adUnit.unitId)
            .forNativeAd { ad : NativeAd ->
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Timber.tag("nt.dung").e(adError.message)
                    onAdFailedToLoad()
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build())
            .build()
    }

    DisposableEffect(adUnit) {
        adLoader.loadAd(AdRequestFactory.create())
        onDispose {
            nativeAd?.destroy()
        }
    }


}