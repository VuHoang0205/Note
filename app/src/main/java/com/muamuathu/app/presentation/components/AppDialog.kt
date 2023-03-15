package com.muamuathu.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.muamuathu.app.R
import com.muamuathu.app.presentation.components.dialog.DialogConfirmation
import com.muamuathu.app.presentation.components.dialog.DialogOneButton
import com.muamuathu.app.presentation.event.DialogEvent
import com.muamuathu.app.presentation.event.EventHandler

@Composable
fun AppDialog(
    eventHandler: EventHandler
) {

    when (val event = eventHandler.dialogEvent()) {
        is DialogEvent.None -> {

        }
        is DialogEvent.Loading -> {
            DialogLoading(eventHandler)
        }
        is DialogEvent.Confirmation -> DialogConfirmation(
            eventHandler,
            stringResource(id = event.title),
            stringResource(id = event.message),
            stringResource(id = event.cancelText),
            event.cancelClick,
            event.cancelColor,
            stringResource(id = event.confirmText),
            event.confirmClick,
            event.confirmColor,
        )
        is DialogEvent.Error -> DialogOneButton(
            eventHandler = eventHandler,
            title = stringResource(id = R.string.error),
            message = event.message,
            buttonText = stringResource(id = R.string.close),
            buttonClick = event.onConfirm
        )
    }
}

@Composable
fun DialogLoading(
    eventHandler: EventHandler,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties()
) {
    DialogBase(eventHandler, onDismissRequest, properties) {
        AwairWrapperLoading()
    }
}

@Composable
fun AwairWrapperLoading() {
    Column(
        modifier = Modifier.wrapContentSize(align = Alignment.Center)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(6.dp)),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
internal fun DialogBase(
    eventHandler: EventHandler,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = {
        onDismissRequest.invoke()
        eventHandler.postDialogEvent(DialogEvent.None)
    }, properties = properties) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), shape = RoundedCornerShape(12.dp)) {
            content.invoke()
        }
    }
}
