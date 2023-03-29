package com.muamuathu.app.presentation.ui.note.viewModel

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.domain.model.Tag
import com.muamuathu.app.domain.model.commons.WrapList
import com.muamuathu.app.presentation.helper.resultFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    internal val dateTime = MutableStateFlow(Calendar.getInstance().timeInMillis)
    internal val folder = MutableStateFlow(Folder())
    internal val tags = MutableStateFlow(WrapList(emptyList<Tag>()))
    internal val title = MutableStateFlow("")
    internal val content = MutableStateFlow("")
    internal val attachments = MutableStateFlow(WrapList(emptyList<String>()))
    internal val isValidData = MutableStateFlow(false)

    fun updateFolder(folder: Folder) {
        this.folder.tryEmit(folder)
        checkValidData()
    }

    fun updateAttachments(attachments: List<String>) {
        val list = mutableListOf<String>()
        list.addAll(attachments)
        list.addAll(this.attachments.value.list)
        this.attachments.tryEmit(WrapList(list))
    }

    fun updateTag(tag: List<Tag>) {
        this.tags.tryEmit(WrapList(tag))
    }

    fun updateDateTime(time: Long) {
        dateTime.tryEmit(time)
        checkValidData()
    }

    fun updateTitle(title: String) {
        this.title.tryEmit(title)
        checkValidData()
    }

    fun updateContent(content: String) {
        this.content.tryEmit(content)
        checkValidData()
    }

    private fun checkValidData() {
        isValidData.tryEmit(
            dateTime.value > 0L
                    && !TextUtils.isEmpty(folder.value.name)
                    && !TextUtils.isEmpty(title.value)
                    && !TextUtils.isEmpty(content.value)

        )
    }

    fun saveNote() = resultFlow {
        repo.saveNote(Note(
            title = title.value,
            content = content.value,
            dateTime = dateTime.value,
            attachments = attachments.value.list,
            tags = tags.value.list
        ))
    }

    fun clearReference() {
        folder.value = Folder()
        tags.value = WrapList(emptyList())
        attachments.value = WrapList(emptyList())
        isValidData.value = false
        title.value = ""
        content.value = ""
        dateTime.value = 0L
    }
}