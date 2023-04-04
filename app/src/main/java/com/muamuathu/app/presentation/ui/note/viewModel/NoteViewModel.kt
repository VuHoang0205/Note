package com.muamuathu.app.presentation.ui.note.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.base.MockData
import com.muamuathu.app.data.base.MockData.getNote
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.presentation.helper.ResultWrapper
import com.muamuathu.app.presentation.helper.resultFlow
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    val dateListStateFlow = MutableStateFlow<List<ZonedDateTime>>(emptyList())
    val noteListStateFlow = MutableStateFlow<List<Note>>(mutableListOf())

    fun getCalenderList(
        zonedDateTime: ZonedDateTime,
    ) = ioLaunch {
        MockData.getCalendarList(zonedDateTime).flowOn(Dispatchers.IO).collect {
            dateListStateFlow.value = it
        }
    }

    fun getNoteList() = resultFlow {
        repo.loadNote().apply {
            if (this is ResultWrapper.Success) {
                noteListStateFlow.tryEmit(value)
            }
        }
    }

    fun removeNote(note: Note) = resultFlow {
        repo.deleteNote(note).apply {
            if (this is ResultWrapper.Success) {
                val list = mutableListOf<Note>()
                list.addAll(noteListStateFlow.value)
                if (list.remove(note)) {
                    noteListStateFlow.tryEmit(list)
                }
            }
        }
    }

    fun getNoteById(idNote: String) = ioLaunch {
        getNote().flowOn(Dispatchers.IO).collect {

        }
    }
}