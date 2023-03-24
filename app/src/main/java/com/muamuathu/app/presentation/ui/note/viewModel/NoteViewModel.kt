package com.muamuathu.app.presentation.ui.note.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.base.MockData
import com.muamuathu.app.data.base.MockData.getNote
import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor() : ViewModel() {

    private val dateListStateFlow = MutableStateFlow<List<ZonedDateTime>>(emptyList())
    private val entityNoteListStateFlow = MutableStateFlow<MutableList<EntityNote>>(mutableListOf())
    private val entityNoteSharedFlow = MutableStateFlow<EntityNote?>(null)

    init {
        getCalenderList(ZonedDateTime.now())
        getNoteList()
    }

    fun getCalenderList(
        zonedDateTime: ZonedDateTime
    ) = ioLaunch {
        MockData.getCalendarList(zonedDateTime).flowOn(Dispatchers.IO).collect {
            dateListStateFlow.value = it
        }
    }

    private fun getNoteList() = ioLaunch {
        MockData.getNoteItemList().flowOn(Dispatchers.IO).collect {
            entityNoteListStateFlow.value = it
        }
    }

    fun removeNote(entityNote: EntityNote) = ioLaunch {
        val newData = mutableListOf<EntityNote>()
        newData.addAll(entityNoteListStateFlow.value)
        newData.remove(entityNote)
        entityNoteListStateFlow.value = newData
    }

    fun getNoteById(idNote: String) = ioLaunch {
        getNote().flowOn(Dispatchers.IO).collect {
            entityNoteSharedFlow.value = it
        }
    }

    fun bindDateListState() = dateListStateFlow.asStateFlow()
    fun bindNoteListState() = entityNoteListStateFlow.asStateFlow()
    fun bindNote() = entityNoteSharedFlow.asSharedFlow()
}