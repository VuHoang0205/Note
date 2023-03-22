package com.muamuathu.app.presentation.ui.folder.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.entity.Folder
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.presentation.extensions.removeAccent
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    private val folderListOriginState: MutableStateFlow<List<Folder>> = MutableStateFlow(ArrayList())
    val folderListState: MutableStateFlow<List<Folder>> = MutableStateFlow(ArrayList())
    var query: MutableStateFlow<String> = MutableStateFlow("")

    init {
        getFolderList()
    }

    private fun getFolderList() = ioLaunch {
        repo.loadFolders().collect {
            folderListOriginState.value = it
            folderListState.value = it
        }
    }

    fun searchFolder(textSearch: String) = ioLaunch {
        query.value = textSearch
        if (!folderListOriginState.value.isEmpty()) {
            if (textSearch.isNotEmpty()) {
                val folderList = folderListOriginState.value.toMutableList()
                val searchText = textSearch.removeAccent()
                val result = folderList.filter {
                    it.name.removeAccent().contains(searchText)
                }
                folderListState.value = result
            } else {
                folderListState.value = folderListOriginState.value
            }
        }
    }
}