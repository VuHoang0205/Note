package com.muamuathu.feature

import com.muamuathu.feature.ads.BuildConfig

sealed class AdUnit(val unitId: String) {
    object TestBanner: AdUnit("ca-app-pub-3940256099942544/6300978111")
    object TestNative: AdUnit("ca-app-pub-3940256099942544/2247696110")
    object TestNativeVideo: AdUnit("ca-app-pub-3940256099942544/1044960115")
    object TestInterstitial: AdUnit("ca-app-pub-3940256099942544/1033173712")
    object TestInterstitialVideo: AdUnit("ca-app-pub-3940256099942544/8691691433")
    object TestAppOpen: AdUnit("ca-app-pub-3940256099942544/3419835294")
    object TestRewarded: AdUnit("ca-app-pub-3940256099942544/5224354917")
    object TestRewardedInterstitial: AdUnit("ca-app-pub-3940256099942544/5354046379")

    object Banner : AdUnit(BuildConfig.banner)
    object Interstitial: AdUnit(BuildConfig.interstitial)
}