package com.muamuathu.app.presentation.helper

import com.muamuathu.app.data.base.AppLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeDataBaseCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T,
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            AppLog.e("API Error: ${throwable.message}")
            ResultWrapper.GenericError(throwable)
        }
    }
}

