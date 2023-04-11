package com.muamuathu.app.presentation.ui.todo.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.model.SubTask
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SubTasksViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    val subTaskFlow = MutableStateFlow<List<SubTask>>(emptyList())

    init {
        getSubTasks()
    }

    private fun getSubTasks() = ioLaunch {
        repo.getSubTasks().collect {
            subTaskFlow.value = it
        }
    }

    fun saveSubTask(subTask: SubTask) = ioLaunch {
        repo.saveSubTask(subTask)
    }
}