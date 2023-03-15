package com.muamuathu.app.presentation.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.muamuathu.app.presentation.event.EventHandler


@Composable
internal fun DialogBase(
    eventHandler: EventHandler,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = {
        onDismissRequest.invoke()
    }, properties = properties) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), shape = RoundedCornerShape(12.dp)) {
            content.invoke()
        }
    }
}


@Composable
fun DialogMessageContent(
    title: String,
    message: CharSequence
) {
    Column {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 24.dp, end = 20.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        val align = TextAlign.Center
        val modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)

        if (message is AnnotatedString) {
            Text(
                text = message,
                textAlign = align,
                modifier = modifier
            )
        } else {
            Text(
                text = message.toString(),
                textAlign = align,
                modifier = modifier
            )
        }
    }
}

@Composable
@Preview
private fun DialogMessageStringContent() {
    DialogMessageContent("The dialog use string", "Test")
}