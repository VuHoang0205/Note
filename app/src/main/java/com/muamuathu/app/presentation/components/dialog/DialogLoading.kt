package com.muamuathu.app.presentation.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.muamuathu.app.presentation.nav.EventHandler

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
@Preview
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