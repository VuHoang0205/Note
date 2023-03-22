package com.muamuathu.app.presentation.ui.folder.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muamuathu.app.data.entity.Folder
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.presentation.extensions.removeAccent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    private val folderListOriginState: MutableState<List<Folder>> = mutableStateOf(ArrayList())
    val folderListState: MutableState<List<Folder>> = mutableStateOf(ArrayList())
    var query: MutableState<String> = mutableStateOf("")

    init {
        getFolderList()
    }

    private fun getFolderList() = viewModelScope.launch {
        repo.loadFolders().collect {
            folderListOriginState.value = it
            folderListState.value = it
        }
    }

    fun searchFolder(textSearch: String) {
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