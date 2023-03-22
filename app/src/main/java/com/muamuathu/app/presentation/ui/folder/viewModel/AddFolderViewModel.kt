package com.muamuathu.app.presentation.ui.folder.viewModel

import com.muamuathu.app.data.entity.Folder
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.presentation.common.BaseViewModel
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddFolderViewModel @Inject constructor(private val repo: JournalRepo) : BaseViewModel() {

    val folderListState: MutableStateFlow<List<Folder>> = MutableStateFlow(emptyList())

    init {
        loadSelectFolders()
    }

    fun saveFolder(folder: Folder) = ioLaunch {
        repo.addFolder(folder)
    }

    private fun loadSelectFolders() = ioLaunch {
        repo.loadFolders()
    }
}