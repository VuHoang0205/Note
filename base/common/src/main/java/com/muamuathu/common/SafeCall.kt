package com.muamuathu.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

suspend fun <T> safeCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    call: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(call.invoke())
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            ResultWrapper.Error(throwable)
        }
    }
}