package com.muamuathu.app.presentation.ui.todo.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.base.MockData
import com.muamuathu.app.data.entity.EntityTask
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor() : ViewModel() {

    private val dateListStateFlow = MutableStateFlow<List<ZonedDateTime>>(emptyList())
    private val entityTaskListStateFlow = MutableStateFlow<MutableList<EntityTask>>(mutableListOf())

    init {
        getCalenderList(ZonedDateTime.now())
        getTaskList()
    }

    fun getCalenderList(
        zonedDateTime: ZonedDateTime,
    ) = ioLaunch {
        MockData.getCalendarList(zonedDateTime).flowOn(Dispatchers.IO).collect {
            dateListStateFlow.value = it
        }
    }

    private fun getTaskList() = ioLaunch {
        MockData.getMockTaskList().flowOn(Dispatchers.IO).collect {
            entityTaskListStateFlow.value = it
        }
    }

    fun removeTask(selectEntityTask: EntityTask) = ioLaunch {
        val newData = mutableListOf<EntityTask>()
        newData.addAll(entityTaskListStateFlow.value)
        newData.remove(selectEntityTask)
        entityTaskListStateFlow.value = newData
    }

    fun bindDateListState() = dateListStateFlow.asStateFlow()
    fun bindTaskListState() = entityTaskListStateFlow.asStateFlow()
}