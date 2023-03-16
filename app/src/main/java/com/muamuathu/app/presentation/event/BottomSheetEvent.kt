package com.muamuathu.app.presentation.event

import androidx.compose.runtime.Composable

sealed class BottomSheetEvent {
    object None : BottomSheetEvent()
    class Hide(val onHide: () -> Unit) : BottomSheetEvent()

    fun isShowing(): Boolean {
        return this != None && this !is Hide
    }

    class Custom(val ui: @Composable () -> Unit) : BottomSheetEvent()
}