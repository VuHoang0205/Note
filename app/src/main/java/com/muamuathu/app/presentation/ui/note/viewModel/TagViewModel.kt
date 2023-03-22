package com.muamuathu.app.presentation.ui.note.viewModel

import androidx.lifecycle.viewModelScope
import com.muamuathu.app.data.entity.Tag
import com.muamuathu.app.presentation.common.BaseViewModel
import com.muamuathu.app.data.repository.JournalRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(private val repo: JournalRepo) : BaseViewModel() {

    val tagListState: MutableStateFlow<List<Tag>> = MutableStateFlow(emptyList())

    init {
        loadTags()
    }

    fun saveTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO) {
        repo.addTag(tag)
    }

    private fun loadTags() = viewModelScope.launch {
        repo.loadTags()
            .flowOn(baseCoroutineContext)
            .collect {
                tagListState.value = it
            }
    }
}