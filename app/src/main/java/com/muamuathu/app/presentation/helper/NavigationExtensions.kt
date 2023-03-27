package com.muamuathu.app.presentation.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
inline fun <T> observeNavigationResultOnce(
    key: String = "result",
    navController: NavController = rememberNavController(),
    crossinline  block: @Composable (T) -> (Unit),
) = run {
    val observeAsState = navController.getNavigationResult<T>(key)?.observeAsState()
    observeAsState?.value?.let {
        block.invoke(it)
        navController.removeNavigationResult<T>(key)
    }
}

fun <T> NavController.getNavigationResult(
    key: String = "result",
): LiveData<T>? {
    return currentBackStackEntry?.savedStateHandle?.getLiveData(key)
}

fun <T> NavController.removeNavigationResult(key: String = "result") {
    currentBackStackEntry?.savedStateHandle?.remove<T>(key)
}