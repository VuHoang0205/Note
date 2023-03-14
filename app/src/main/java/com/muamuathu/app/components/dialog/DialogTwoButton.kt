package com.muamuathu.app.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muamuathu.app.event.DialogEvent
import com.muamuathu.app.nav.EventHandler

@Composable
fun DialogTwoButton(
    eventHandler: EventHandler,
    title: String,
    message: CharSequence,
    leftButtonText: String,
    leftButtonTextColor: Color = MaterialTheme.colorScheme.primary,
    leftButtonClick: () -> Unit = {},
    rightButtonText: String,
    rightButtonTextColor: Color = MaterialTheme.colorScheme.primary,
    rightButtonClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
    topContent: @Composable () -> Unit = {},
    centerContent: @Composable () -> Unit = {}
) {
    DialogBase(eventHandler, onDismissRequest, properties) {
        Content(
            title, message,
            leftButtonText,
            leftButtonTextColor, {
                eventHandler.postDialogEvent(DialogEvent.None)
                leftButtonClick.invoke()
            },
            rightButtonText,
            rightButtonTextColor,
            {
                eventHandler.postDialogEvent(DialogEvent.None)
                rightButtonClick.invoke()
            },
            topContent, centerContent
        )
    }
}

@Composable
private fun Content(
    title: String,
    message: CharSequence,
    leftButtonText: String,
    leftButtonTextColor: Color = MaterialTheme.colorScheme.primary,
    leftButtonClick: () -> Unit = {},
    rightButtonText: String,
    rightButtonTextColor: Color = MaterialTheme.colorScheme.primary,
    rightButtonClick: () -> Unit = {},
    topContent: @Composable () -> Unit = {},
    centerContent: @Composable () -> Unit = {},
) {
    Column {
        topContent.invoke()
        DialogMessageContent(
            title = title,
            message = message
        )
        centerContent.invoke()
        Divider(color = Gray, thickness = 0.5.dp)
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (left, space, right) = createRefs()
            Spacer(
                Modifier
                    .constrainAs(space) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    }
                    .width(0.5.dp)
                    .background(Gray))

            TextButton(modifier = Modifier.constrainAs(left) {
                start.linkTo(parent.start)
                end.linkTo(space.start)
                width = Dimension.fillToConstraints
            },
                onClick = {
                    leftButtonClick.invoke()
                }) {
                Text(
                    text = leftButtonText,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    color = leftButtonTextColor,
                    textAlign = TextAlign.Center
                )
            }
            TextButton(modifier = Modifier.constrainAs(right) {
                start.linkTo(space.end)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, onClick = {
                rightButtonClick.invoke()
            }) {
                Text(
                    text = rightButtonText,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    color = rightButtonTextColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
private fun DialogTwoButton() {
    Content(title = "Title", message = buildAnnotatedString {
        append("Message")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(" style ")
        }
    }, leftButtonText = "Start", rightButtonText = "Exit", rightButtonTextColor = Red) {

    }
}