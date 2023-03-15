package com.muamuathu.app.presentation.common.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.muamuathu.app.presentation.common.BaseViewModel
import com.muamuathu.app.presentation.event.FloatingButtonEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FloatingButtonViewModel @Inject constructor() : BaseViewModel() {
    var event by mutableStateOf<FloatingButtonEvent>(FloatingButtonEvent.None)
}