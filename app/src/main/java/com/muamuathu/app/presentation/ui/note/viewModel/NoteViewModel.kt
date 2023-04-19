package com.muamuathu.app.presentation.ui.note.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.base.MockData
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.domain.model.commons.WrapList
import com.muamuathu.app.presentation.extensions.removeAccent
import com.muamuathu.app.presentation.extensions.toTimeInMillis
import com.muamuathu.app.presentation.helper.ResultWrapper
import com.muamuathu.app.presentation.helper.resultFlow
import com.muamuathu.common.ioLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    val dateListStateFlow = MutableStateFlow<List<ZonedDateTime>>(emptyList())
    val noteListStateFlow = MutableStateFlow<WrapList<Note>>(WrapList(emptyList()))
    private val noteListListOriginStateFlow: MutableStateFlow<List<Note>> =
        MutableStateFlow(ArrayList())
    var query: MutableStateFlow<String> = MutableStateFlow("")

    init {
        getCalenderList(ZonedDateTime.now())
        getNoteList(ZonedDateTime.now().toTimeInMillis())
    }

    private fun getCalenderList(
        zonedDateTime: ZonedDateTime,
    ) = ioLaunch {
        MockData.getCalendarList(zonedDateTime).flowOn(Dispatchers.IO).collect {
            dateListStateFlow.value = it
        }
    }

     fun getNoteList(time: Long) = ioLaunch {
        repo.loadNote(time).collect {
            noteListListOriginStateFlow.value = it
            noteListStateFlow.tryEmit(WrapList(it))
        }
    }

    fun removeNote(note: Note) = resultFlow {
        repo.deleteNote(note).apply {
            if (this is ResultWrapper.Success) {
                val list = noteListStateFlow.value.list.toMutableList()
                if (list.remove(note)) {
                    noteListStateFlow.tryEmit(WrapList(list))
                }
            }
        }
    }

    fun getNoteById(idNote: Long) = resultFlow {
        repo.getNoteById(idNote)
    }

    fun search(textSearch: String) = ioLaunch {
        query.value = textSearch
        if (noteListListOriginStateFlow.value.isNotEmpty()) {
            if (textSearch.isNotEmpty()) {
                val noteList = noteListListOriginStateFlow.value.toMutableList()
                val searchText = textSearch.removeAccent()
                val result = noteList.filter {
                    it.title.removeAccent().contains(searchText) || it.content.removeAccent()
                        .contains(searchText)
                }
                noteListStateFlow.tryEmit(WrapList(result))
            } else {
                noteListStateFlow.tryEmit(WrapList(noteListListOriginStateFlow.value))
            }
        }
    }
}