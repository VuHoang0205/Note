package com.muamuathu.app.presentation.ui.todo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muamuathu.app.data.base.MockData.getCalendarList
import com.muamuathu.app.data.base.MockData.getMockTaskList
import com.muamuathu.app.data.entity.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor() : ViewModel() {

    private val dateListStateFlow = MutableStateFlow<List<ZonedDateTime>>(emptyList())
    private val taskListStateFlow = MutableStateFlow<MutableList<Task>>(mutableListOf())

    init {
        getCalenderList(ZonedDateTime.now())
        getTaskList()
    }

    fun getCalenderList(
        zonedDateTime: ZonedDateTime,
    ) = viewModelScope.launch {
        getCalendarList(zonedDateTime).flowOn(Dispatchers.IO).collect {
            dateListStateFlow.value = it
        }
    }

    private fun getTaskList() = viewModelScope.launch {
        getMockTaskList().flowOn(Dispatchers.IO).collect {
            taskListStateFlow.value = it
        }
    }

    fun removeTask(selectTask: Task) = viewModelScope.launch {
        val newData = mutableListOf<Task>()
        newData.addAll(taskListStateFlow.value)
        newData.remove(selectTask)
        taskListStateFlow.value = newData
    }

    fun bindDateListState() = dateListStateFlow.asStateFlow()
    fun bindTaskListState() = taskListStateFlow.asStateFlow()
}