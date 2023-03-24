package com.muamuathu.app.presentation.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun <T> ViewModel.resultFlow(
    firstValue: ResultWrapper<T> = ResultWrapper.None,
    callback: suspend () -> ResultWrapper<T>
): MutableStateFlow<ResultWrapper<T>> = MutableStateFlow(firstValue).apply {
    viewModelScope.launch {
        with(this@resultFlow) {
            emit(ResultWrapper.Loading)
            tryEmit(callback.invoke())
        }
    }
}