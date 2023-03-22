package com.muamuathu.app.presentation.ui.folder.viewModel

import androidx.lifecycle.viewModelScope
import com.muamuathu.app.data.entity.Folder
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFolderViewModel @Inject constructor(private val repo: JournalRepo) : BaseViewModel() {

    val folderListState: MutableStateFlow<List<Folder>> = MutableStateFlow(emptyList())

    init {
        loadSelectFolders()
    }

    fun saveFolder(folder: Folder) = viewModelScope.launch {
        repo.addFolder(folder)
    }

    private fun loadSelectFolders() = viewModelScope.launch {
        repo.loadFolders()
            .flowOn(baseCoroutineContext)
            .collect {
                folderListState.value = it
            }
    }
}