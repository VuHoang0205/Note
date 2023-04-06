package com.muamuathu.app.presentation.ui.folder.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.commons.WrapList
import com.muamuathu.app.presentation.extensions.removeAccent
import com.muamuathu.app.presentation.helper.resultFlow
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    private val entityFolderListOriginState: MutableStateFlow<List<Folder>> =
        MutableStateFlow(ArrayList())
    val entityFolderListState: MutableStateFlow<WrapList<Folder>> =
        MutableStateFlow(WrapList(emptyList()))

    fun getFolderList() = ioLaunch {
        repo.loadFolders().collect {
            entityFolderListOriginState.value = it
            entityFolderListState.value = WrapList(it)
        }
    }

    fun searchFolder(textSearch: String) = ioLaunch {
        if (entityFolderListOriginState.value.isNotEmpty()) {
            if (textSearch.isNotEmpty()) {
                val folderList = entityFolderListOriginState.value.toMutableList()
                val searchText = textSearch.removeAccent()
                val result = folderList.filter {
                    it.name.removeAccent().contains(searchText)
                }
                entityFolderListState.value = WrapList(result)
            } else {
                entityFolderListState.value = WrapList(entityFolderListOriginState.value)
            }
        }
    }

    fun updateFolder(folder: Folder) = resultFlow {
        repo.updateFolder(folder)
    }

    fun deleteFolder(folder: Folder) = ioLaunch {
        repo.deleteFolder(folder)
    }

    fun saveFolder(folder: Folder) = resultFlow {
        repo.saveFolder(folder)
    }
}