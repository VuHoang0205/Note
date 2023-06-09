package com.muamuathu.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.ioLaunch(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.IO, block = block)
}