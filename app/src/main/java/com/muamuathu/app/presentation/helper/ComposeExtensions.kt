package com.muamuathu.app.presentation.helper

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
    successHandler: SuccessHandler<T>,
) {
    launch {
        stateFlow.collect {
            when (it) {
                is ResultWrapper.Success<*> -> {
                    successHandler.invoke(it.takeValueOrThrow())
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