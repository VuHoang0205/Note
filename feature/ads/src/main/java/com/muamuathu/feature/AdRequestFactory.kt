package com.muamuathu.feature

import com.google.android.gms.ads.AdRequest

object AdRequestFactory {
    fun create() : AdRequest {
        return AdRequest.Builder().build()
    }
}