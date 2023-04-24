package com.muamuathu.app.presentation.ui.todo.viewModel

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.model.ReminderTypeEnum
import com.muamuathu.app.domain.model.RepeatTypeEnum
import com.muamuathu.app.domain.model.Task
import com.muamuathu.app.presentation.extensions.toTimeInMillis
import com.muamuathu.app.presentation.helper.ResultWrapper
import com.muamuathu.app.presentation.helper.resultFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    internal val dateTime = MutableStateFlow(ZonedDateTime.now().toTimeInMillis())
    internal val task = MutableStateFlow(Task())
    internal val title = MutableStateFlow("")
    internal val content = MutableStateFlow("")
    internal val reminderType = MutableStateFlow(ReminderTypeEnum.OneTime)
    internal val repeatType = MutableStateFlow(RepeatTypeEnum.None)
    internal val isValidData = MutableStateFlow(false)

    fun updateTitle(title: String) {
        this.title.tryEmit(title)
        checkValidData()
    }

    fun updateContent(content: String) {
        this.content.value = content
        checkValidData()
    }

    fun updateDateTime(time: Long) {
        dateTime.value = time
        checkValidData()
    }

    fun updateRepeatType(repeatType: RepeatTypeEnum) {
        this.repeatType.value = repeatType
    }

    fun updateReminderType(reminderTypeEnum: ReminderTypeEnum) {
        this.reminderType.value = reminderTypeEnum
    }

    private fun checkValidData() {
        isValidData.value = dateTime.value > 0L
                && !TextUtils.isEmpty(task.value.name)
                && !TextUtils.isEmpty(title.value)
                && !TextUtils.isEmpty(content.value)
                && task.value.subTasks.isNotEmpty()
    }

    fun saveNote() = resultFlow {
        ResultWrapper.Empty
    }

    fun clearReference() {
        isValidData.value = false
        title.value = ""
        content.value = ""
        dateTime.value = Calendar.getInstance().timeInMillis
    }
}