package com.muamuathu.app.presentation.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

typealias ErrorHandler = (exception: Throwable?) -> Unit
typealias SuccessHandler<T> = (value: T) -> Unit
typealias LoadingHandler = () -> Unit
typealias EmptyHandler = () -> Unit

fun <T> CoroutineScope.observeResultFlow(
    stateFlow: StateFlow<ResultWrapper<T>>,
    errorHandler: ErrorHandler? = null,
    loadingHandler: LoadingHandler? = null,
    successHandler: SuccessHandler<T>? = null,
) {
    launch {
        stateFlow.collect {
            when (it) {
                is ResultWrapper.Success<*> -> {
                    successHandler?.invoke(it.takeValueOrThrow())
                }
                is ResultWrapper.GenericError -> {
                    errorHandler?.invoke(it.throwable)
                }

                is ResultWrapper.Loading -> {
                    loadingHandler?.invoke()
                }
                else -> {
                }
            }
        }
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event:Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}