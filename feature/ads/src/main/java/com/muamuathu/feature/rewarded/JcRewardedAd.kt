package com.muamuathu.feature.rewarded

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.muamuathu.feature.AdRequestFactory
import com.muamuathu.feature.AdUnit

@Composable
fun JcRewardedAd(
    adUnit: AdUnit = AdUnit.TestRewarded,
    show: Boolean,
    onRewardedAdLoaded: () -> Unit = {},
    onUserEarnedReward: (RewardItem) -> Unit
) {
    val activity = LocalContext.current as ComponentActivity
    var rewardedAd: RewardedAd? by remember { mutableStateOf(null) }


    DisposableEffect(Unit) {
        RewardedAd.load(activity,adUnit.unitId, AdRequestFactory.create(), object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                onRewardedAdLoaded()
            }
        })
        onDispose {
            rewardedAd = null
        }
    }

    LaunchedEffect(rewardedAd, show) {
        if (show) {
            rewardedAd?.show(activity) { rewardItem -> onUserEarnedReward(rewardItem) }
        }
    }
}