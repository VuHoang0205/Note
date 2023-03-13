package com.muamuathu.feature.purchase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.feature.purchase.viewmodel.PurchaseViewModel

@Composable
fun isPremiumUser(): State<Boolean> {
    val purchaseViewModel = hiltViewModel<PurchaseViewModel>()
    return produceState(initialValue = false) {
        purchaseViewModel.isPremiumUser().collect { value = it }
    }
}