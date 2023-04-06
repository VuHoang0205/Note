package com.muamuathu.app.presentation.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
        Card(elevation = CardDefaults.cardElevation(2.dp), shape = RoundedCornerShape(12.dp)) {
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

@Composable
fun JcDialog(
    title: String,
    message: String,
    neutralButtonText: String = "",
    negativeButtonText: String,
    positiveButtonText: String,
    onNegativeClicked: () -> Unit,
    onPositiveClicked: () -> Unit,
    onNeutralClicked: () -> Unit = {},
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {

                Text(
                    modifier = Modifier,
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier,
                    text = message,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (neutralButtonText.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .clickable { onNeutralClicked() },
                            text = neutralButtonText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (negativeButtonText.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .clickable { onNegativeClicked() }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            text = negativeButtonText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        modifier = Modifier
                            .clickable { onPositiveClicked() }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        text = positiveButtonText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}