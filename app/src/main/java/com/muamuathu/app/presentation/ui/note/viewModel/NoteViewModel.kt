package com.muamuathu.app.presentation.ui.note.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.base.MockData
import com.muamuathu.app.data.base.MockData.getNote
import com.muamuathu.app.data.entity.Note
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
    private val noteListStateFlow = MutableStateFlow<MutableList<Note>>(mutableListOf())
    private val noteSharedFlow = MutableStateFlow<Note?>(null)

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
            noteListStateFlow.value = it
        }
    }

    fun removeNote(note: Note) = ioLaunch {
        val newData = mutableListOf<Note>()
        newData.addAll(noteListStateFlow.value)
        newData.remove(note)
        noteListStateFlow.value = newData
    }

    fun getNoteById(idNote: String) = ioLaunch {
        getNote().flowOn(Dispatchers.IO).collect {
            noteSharedFlow.value = it
        }
    }

    fun bindDateListState() = dateListStateFlow.asStateFlow()
    fun bindNoteListState() = noteListStateFlow.asStateFlow()
    fun bindNote() = noteSharedFlow.asSharedFlow()
}