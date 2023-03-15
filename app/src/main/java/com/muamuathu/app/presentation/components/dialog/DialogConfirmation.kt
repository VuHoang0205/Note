package com.muamuathu.app.presentation.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.muamuathu.app.presentation.event.EventHandler

@Composable
fun DialogConfirmation(
    eventHandler: EventHandler,
    title: String,
    message: CharSequence,
    cancelText: String,
    cancelClick: () -> Unit,
    cancelColor: Color,
    confirmText: String,
    confirmClick: () -> Unit,
    confirmColor: Color
) {
    DialogTwoButton(
        eventHandler = eventHandler,
        title = title,
        message = message,
        leftButtonText = cancelText,
        leftButtonClick = cancelClick,
        leftButtonTextColor = cancelColor,
        rightButtonText = confirmText,
        rightButtonTextColor = confirmColor,
        rightButtonClick = confirmClick
    )
}