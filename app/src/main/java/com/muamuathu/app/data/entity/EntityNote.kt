package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.muamuathu.app.presentation.extensions.getStartOfDay

@Entity
data class EntityNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val content: String,
    val dateTime: Long,
    val startOfDay: Long = dateTime.getStartOfDay(),
    val attachments: List<String>,
)