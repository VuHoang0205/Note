package com.muamuathu.app.presentation.event

import androidx.compose.ui.graphics.Color
import com.muamuathu.app.R

sealed class DialogEvent {
    object None : DialogEvent()
    object Loading : DialogEvent()
    class Confirmation(
        val title: Int,
        val message: Int,
        val confirmText: Int,
        val confirmColor: Color = Color.Red,
        val cancelText: Int = R.string.cancel,
        val cancelClick: () -> Unit = {},
        val cancelColor: Color,
        val confirmClick: () -> Unit
    ) : DialogEvent()

    class Error(
        val message: String,
        val onConfirm: () -> Unit
    ) : DialogEvent()
}
