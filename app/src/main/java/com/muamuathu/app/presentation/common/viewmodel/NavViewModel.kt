package com.muamuathu.app.presentation.common.viewmodel

import com.muamuathu.app.presentation.common.BaseViewModel
import com.muamuathu.app.presentation.event.NavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(): BaseViewModel() {
    var event = MutableStateFlow<NavEvent>(NavEvent.None)
}

