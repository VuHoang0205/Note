package com.muamuathu.app.presentation.components

import androidx.compose.runtime.Composable
import com.muamuathu.app.presentation.nav.EventHandler

@Composable
fun AppTopBar(eventHandler: EventHandler) {
//    when (val event = eventHandler.topBarEvent()) {
//        is TopBarEvent.Title -> TopBarBase(
//            title = event.title,
//            titleAlign = event.titleAlign,
//            navigationIcon = null,
//            listRightIcon = event.rightList
//        )
//        is TopBarEvent.Back -> TopBarBase(
//            title = event.title,
//            titleAlign = event.titleAlign,
//            navigationIcon = {
//                IconButton(
//                    modifier = Modifier
//                        .wrapContentSize(),
//                    onClick = event.onBack
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = ""
//                    )
//                }
//            },
//            listRightIcon = event.rightList
//        )
//        is TopBarEvent.Close -> TopBarBase(
//            title = event.title,
//            titleAlign = event.titleAlign,
//            navigationIcon = {
//                IconButton(
//                    modifier = Modifier
//                        .wrapContentSize(),
//                    onClick = event.onClose
//                ) {
//                    Icon(
//                        Icons.Default.Close,
//                        contentDescription = ""
//                    )
//                }
//            },
//            listRightIcon = event.rightList
//        )
//        is TopBarEvent.Home -> TopBarBase(
//            title = event.title,
//            titleAlign = event.titleAlign,
//            navigationIcon = {
//                IconButton(
//                    modifier = Modifier
//                        .wrapContentSize(),
//                    onClick = event.onHome
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Home,
//                        contentDescription = ""
//                    )
//                }
//            },
//            listRightIcon = event.rightList
//        )
//        is TopBarEvent.None -> {}
//    }
}
