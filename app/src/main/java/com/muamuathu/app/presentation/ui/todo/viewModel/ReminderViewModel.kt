package com.muamuathu.app.presentation.ui.todo.viewModel

import androidx.lifecycle.ViewModel
import com.muamuathu.app.data.repository.JournalRepo
import com.muamuathu.app.domain.model.ReminderTypeEnum
import com.muamuathu.app.domain.model.RepeatTypeEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(private val repo: JournalRepo) : ViewModel() {

    internal val reminderType = MutableStateFlow(ReminderTypeEnum.OneTime)
    internal val repeatType = MutableStateFlow(RepeatTypeEnum.None)
    internal val isValidData = MutableStateFlow(false)
    internal val reminderTime = MutableStateFlow(0L)

    fun updateRepeatType(repeatType: RepeatTypeEnum) {
        this.repeatType.value = repeatType
        checkValidData()
    }

    fun updateReminderType(reminderTypeEnum: ReminderTypeEnum) {
        this.reminderType.value = reminderTypeEnum
        checkValidData()
    }

    fun updateReminderTime(time: Long) {
        this.reminderTime.value = time
        checkValidData()
    }

    private fun checkValidData() {
        isValidData.value = reminderTime.value > 0 && (reminderType.value == ReminderTypeEnum.Repeating && repeatType.value != RepeatTypeEnum.None)
    }
}