package com.muamuathu.app.presentation.ui.note.viewModel

import com.muamuathu.app.data.entity.Tag
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.presentation.common.BaseViewModel
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(private val repo: JournalRepo) : BaseViewModel() {

    val tagListState: MutableStateFlow<List<Tag>> = MutableStateFlow(emptyList())

    init {
        loadTags()
    }

    fun saveTag(tag: Tag) = ioLaunch {
        repo.addTag(tag)
    }

    private fun loadTags() = ioLaunch {
        repo.loadTags()
            .flowOn(baseCoroutineContext)
            .collect {
                tagListState.value = it
            }
    }
}