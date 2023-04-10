package com.muamuathu.app.domain.model

data class SubTask(
    val subTaskId: Long = 0,
    val taskId: Long = 0L,
    val name: String = "",
    val reminderTime: Long = 0L,
    val isDone: Boolean = false
)