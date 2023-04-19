package com.muamuathu.app.presentation.ui.todo.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.domain.model.SubTask
import com.muamuathu.app.domain.model.commons.WrapList
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SubTasksViewModel @Inject constructor() : ViewModel() {

    val subTaskFlow = MutableStateFlow<WrapList<SubTask>>(WrapList(emptyList()))
    private val subTaskOriginFlow = MutableStateFlow<WrapList<SubTask>>(WrapList(emptyList()))

    fun addSubTask(subTask: SubTask) = ioLaunch {
        val subTasks = subTaskFlow.value.list.toMutableList()
        subTasks.add(subTask)
        subTaskFlow.value = WrapList(subTasks)
    }

    fun removeSubTask(subTask: SubTask) = ioLaunch {
        val subTasks = subTaskFlow.value.list.toMutableList()
        subTasks.remove(subTask)
        subTaskFlow.value = WrapList(subTasks)
    }


    fun saveSubTaskOrigin() {
        subTaskOriginFlow.value = subTaskFlow.value
    }

    fun filterSubTaskValid(currentTime: Long): MutableStateFlow<WrapList<SubTask>> {
        val subTasks = subTaskOriginFlow.value.list.toMutableList()
        subTaskFlow.value = WrapList(subTasks.filter { it.reminderTime >= currentTime })
        return subTaskFlow
    }
}