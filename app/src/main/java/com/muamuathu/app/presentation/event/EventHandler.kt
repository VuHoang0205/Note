package com.muamuathu.app.presentation.event

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.presentation.common.viewmodel.*


class EventHandler(
    private val navigationViewModel: NavViewModel,
    private val bottomSheetViewModel: BottomSheetViewModel,
    private val floatingButtonViewModel: FloatingButtonViewModel,
    private val snackBarViewModel: SnackBarViewModel,
    private val topBarViewModel: TopBarViewModel,
    private val dialogViewModel: DialogViewModel
) {
    fun postNavEvent(event: NavEvent) {
        navigationViewModel.event.value = event
    }

    fun postFloatingButtonEvent(event: FloatingButtonEvent) {
        floatingButtonViewModel.event = event
    }

    fun postTopBarEvent(event: TopBarEvent) {
        topBarViewModel.event = event
    }

    fun postBottomSheetEvent(event: BottomSheetEvent) {
        bottomSheetViewModel.event = event
    }

    fun postSnackBarEvent(event: SnackBarEvent) {
        snackBarViewModel.event = event
    }

    fun postDialogEvent(event: DialogEvent) {
        dialogViewModel.event = event
    }

    fun navEvent() = navigationViewModel.event
    fun snackBarEvent() = snackBarViewModel.event
    fun floatingButtonEvent() = floatingButtonViewModel.event
    fun topBarEvent() = topBarViewModel.event
    fun dialogEvent() = dialogViewModel.event
    fun bottomSheetEvent() = bottomSheetViewModel.event

}

@Composable
fun initEventHandler(): EventHandler {
    val activityScope = LocalContext.current as ComponentActivity
    return EventHandler(
        hiltViewModel(activityScope),
        hiltViewModel(activityScope),
        hiltViewModel(activityScope),
        hiltViewModel(activityScope),
        hiltViewModel(activityScope),
        hiltViewModel(activityScope)
    )
}