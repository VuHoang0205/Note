package com.solid.journal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long,
    val title: String,
    val content: String,
    val avatar: String,
    val tag: String,
    val dateTime: Long,
    val attachments: List<String> = emptyList(),
)