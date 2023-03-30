package com.muamuathu.app.presentation.components.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.muamuathu.app.presentation.event.BottomSheetEvent
import com.muamuathu.app.presentation.event.EventHandler

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HandleBottomSheetDisplay(
    eventHandler: EventHandler,
    bottomState: BottomSheetScaffoldState
) {
    when (val event = eventHandler.bottomSheetEvent()) {
        is BottomSheetEvent.None -> LaunchedEffect(
            key1 = event.hashCode(),
            block = {
                bottomState.bottomSheetState.expand()
            })
        is BottomSheetEvent.Hide -> LaunchedEffect(
            key1 = event.hashCode(),
            block = {
                event.onHide.invoke()
                bottomState.bottomSheetState.collapse()
            })
        is BottomSheetEvent.Custom -> LaunchedEffect(
            key1 = event.hashCode(), block = {
                bottomState.bottomSheetState.expand()
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

@OptIn(ExperimentalMaterialApi::class)
fun handleBottomStateChange(
    eventHandler: EventHandler,
    it: BottomSheetScaffoldState
): Boolean {
    if (it.bottomSheetState.isCollapsed) {
        handleHideBottomSheet(eventHandler)
    }
    return true
}