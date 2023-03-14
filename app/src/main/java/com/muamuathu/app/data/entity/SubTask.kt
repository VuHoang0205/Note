package com.solid.journal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubTask(
    @PrimaryKey(autoGenerate = true)
    val subTaskId: Long = 0,
    val taskId: Long,
    val name: String,
    val isDone: Boolean
)