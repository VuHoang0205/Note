package com.muamuathu.app.presentation.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel


open class BaseViewModel : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("ViewModel", "Coroutine function throw an exception >> ${throwable.message}")
    }

    protected val baseCoroutineContext = Dispatchers.IO + coroutineExceptionHandler

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}