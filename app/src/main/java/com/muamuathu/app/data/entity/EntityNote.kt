package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityNote(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long,
    val title: String,
    val content: String,
    val dateTime: Long,
    val attachments: List<String>,
)