package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntitySubTask(
    @PrimaryKey(autoGenerate = true)
    val subTaskId: Long = 0,
    val taskId: Long,
    val name: String,
    val reminderTime: Long,
    val isDone: Boolean
)