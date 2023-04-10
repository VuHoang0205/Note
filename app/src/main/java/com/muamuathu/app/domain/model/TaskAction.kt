package com.muamuathu.app.domain.model

import com.muamuathu.app.R

enum class TaskAction(val resource: Int) {
    AddSubTask(R.drawable.ic_sub_task),
    AddReminder(R.drawable.ic_task_reminder),
    AddPriority(R.drawable.ic_task_priority),
}