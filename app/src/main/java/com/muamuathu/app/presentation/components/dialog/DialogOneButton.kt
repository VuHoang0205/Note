package com.muamuathu.app.presentation.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.muamuathu.app.presentation.event.DialogEvent
import com.muamuathu.app.presentation.event.EventHandler


@Composable
fun DialogOneButton(
    eventHandler: EventHandler,
    title: String,
    message: CharSequence,
    buttonText: String,
    buttonTextColor: Color = MaterialTheme.colorScheme.primary,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
    topContent: @Composable () -> Unit = {},
    centerContent: @Composable () -> Unit = {},
    buttonClick: () -> Unit = {}
) {
    DialogBase(eventHandler, onDismissRequest, properties) {
        Content(title, message, buttonText, buttonTextColor, topContent, centerContent) {
            buttonClick.invoke()
            eventHandler.postDialogEvent(DialogEvent.None)
        }
    }
}

@Composable
private fun Content(
    title: String,
    message: CharSequence,
    buttonText: String,
    textColor: Color = MaterialTheme.colorScheme.primary,
    topContent: @Composable () -> Unit = {},
    centerContent: @Composable () -> Unit = {},
    buttonClick: () -> Unit
) {
    Column {
        topContent.invoke()
        DialogMessageContent(
            title = title,
            message = message
        )
        centerContent.invoke()
        Divider(color = Color.Gray, thickness = 0.5.dp)
        TextButton(onClick = {
            buttonClick.invoke()
        }) {
            Text(
                text = buttonText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview
private fun DialogOneRedButton() {
    Content("Title", buildAnnotatedString {
        append("Message")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(" style ")
        }
    }, "Red close", Red) {

    }
}