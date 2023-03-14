package com.muamuathu.app.presentation.event

sealed class BottomSheetEvent {
    object None : BottomSheetEvent()
    class Hide(val onHide: () -> Unit) : BottomSheetEvent()

    fun isShowing(): Boolean {
        return this != None && this !is Hide
    }

}