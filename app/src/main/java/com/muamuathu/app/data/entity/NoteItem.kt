package com.solid.journal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteItem(
    @PrimaryKey(autoGenerate = true)
    val noteItemId: Long = 0,
    val noteId: Long,
    val type: Int
)