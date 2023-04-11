package com.muamuathu.app.presentation.ui.note.viewModel

import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.model.Tag
import com.muamuathu.app.presentation.common.BaseViewModel
import com.muamuathu.app.presentation.helper.resultFlow
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(private val repo: JournalRepo) : BaseViewModel() {

    val tagListState: MutableStateFlow<List<Tag>> = MutableStateFlow(emptyList())

    init {
        loadTags()
    }

    fun saveTag(tag: Tag) = resultFlow {
        repo.addTag(tag)
    }

    private fun loadTags() = ioLaunch {
        repo.loadTags().collect {
                tagListState.value = it
            }
    }
}