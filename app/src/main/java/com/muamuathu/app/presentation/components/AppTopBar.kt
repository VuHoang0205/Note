package com.muamuathu.app.presentation.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.EventHandler
import com.muamuathu.app.presentation.event.TopBarEvent

@Composable
fun AppTopBar(eventHandler: EventHandler) {
    when (val event = eventHandler.topBarEvent()) {
        is TopBarEvent.Title -> TopBarBase(
            title = event.title,
            titleAlign = event.titleAlign,
            navigationIcon = null,
            listRightIcon = event.rightList
        )
        is TopBarEvent.Back -> TopBarBase(
            title = event.title,
            titleAlign = event.titleAlign,
            navigationIcon = {
                IconButton(
                    modifier = Modifier
                        .wrapContentSize(),
                    onClick = event.onBack
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
            listRightIcon = event.rightList
        )
        is TopBarEvent.Close -> TopBarBase(
            title = event.title,
            titleAlign = event.titleAlign,
            navigationIcon = {
                IconButton(
                    modifier = Modifier
                        .wrapContentSize(),
                    onClick = event.onClose
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = ""
                    )
                }
            },
            listRightIcon = event.rightList
        )
        is TopBarEvent.Home -> TopBarBase(
            title = event.title,
            titleAlign = event.titleAlign,
            navigationIcon = {
                IconButton(
                    modifier = Modifier
                        .wrapContentSize(),
                    onClick = event.onHome
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = ""
                    )
                }
            },
            listRightIcon = event.rightList
        )
        is TopBarEvent.None -> {}
    }
}
