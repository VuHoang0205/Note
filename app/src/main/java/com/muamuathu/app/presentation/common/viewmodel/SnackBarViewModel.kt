package com.muamuathu.app.presentation.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.muamuathu.app.presentation.common.BaseViewModel
import com.muamuathu.app.presentation.event.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SnackBarViewModel @Inject constructor() : BaseViewModel() {
    var event by mutableStateOf<SnackBarEvent>(SnackBarEvent.None)
}