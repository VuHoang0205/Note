package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityNoteItem(
    @PrimaryKey(autoGenerate = true)
    val noteItemId: Long = 0,
    val noteId: Long,
    val type: Int
)