package com.muamuathu.app.presentation.components.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.muamuathu.app.presentation.event.BottomSheetEvent
import com.muamuathu.app.presentation.event.EventHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandleBottomSheetDisplay(
    eventHandler: EventHandler,
    bottomState: BottomSheetScaffoldState
) {
    when (val event = eventHandler.bottomSheetEvent()) {
        is BottomSheetEvent.None -> LaunchedEffect(
            key1 = event.hashCode(),
            block = {
                bottomState.bottomSheetState.hide()
            })
        is BottomSheetEvent.Hide -> LaunchedEffect(
            key1 = event.hashCode(),
            block = {
                event.onHide.invoke()
                bottomState.bottomSheetState.hide()
            })
        is BottomSheetEvent.Custom -> LaunchedEffect(
            key1 = event.hashCode(), block = {
                bottomState.bottomSheetState.show()
            })
    }

    if (eventHandler.bottomSheetEvent().isShowing()) {
        BackHandler {
            handleHideBottomSheet(eventHandler)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    handleHideBottomSheet(eventHandler)
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


@Composable
fun AppBottomSheet(eventHandler: EventHandler) {
    when (val event = eventHandler.bottomSheetEvent()) {
        is BottomSheetEvent.Custom -> event.ui()
        is BottomSheetEvent.None -> BottomSheetEmpty()
        else -> BottomSheetEmpty()
    }
}

private fun handleHideBottomSheet(
    eventHandler: EventHandler
) {
    eventHandler.postBottomSheetEvent(BottomSheetEvent.None)
}

@OptIn(ExperimentalMaterial3Api::class)
fun handleBottomStateChange(
    eventHandler: EventHandler,
    it: SheetValue
): Boolean {
    if (it == SheetValue.Hidden) {
        handleHideBottomSheet(eventHandler)
    }
    return true
}