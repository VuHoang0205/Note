package com.muamuathu.app.presentation.ui.note.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.base.AppLog
import com.muamuathu.app.data.base.MockData
import com.muamuathu.app.data.base.MockData.getNote
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.mapper.toDomainModel
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.domain.model.Tag
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
    val noteSharedFlow = MutableStateFlow<Note?>(null)
    var folder by mutableStateOf(Folder())
    var tags by mutableStateOf(emptyList<Tag>())

    init {
        getCalenderList(ZonedDateTime.now())
        getNoteList()
    }

    fun getCalenderList(
        zonedDateTime: ZonedDateTime,
    ) = ioLaunch {
        MockData.getCalendarList(zonedDateTime).flowOn(Dispatchers.IO).collect {
            dateListStateFlow.value = it
        }
    }

    private fun getNoteList() = ioLaunch {
        repo.loadNote().collect {
            it.forEach {
                AppLog.e("dlaldlasdl: $it")
            }
            noteListStateFlow.value = it
        }
    }

    fun removeNote(note: Note) = resultFlow {
        repo.deleteNote(note)
    }

    fun getNoteById(idNote: String) = ioLaunch {
        getNote().flowOn(Dispatchers.IO).collect {
            noteSharedFlow.value = it.toDomainModel()
        }
    }
}