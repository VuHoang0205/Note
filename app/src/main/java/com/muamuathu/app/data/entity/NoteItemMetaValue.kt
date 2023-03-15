package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteItemMetaValue(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val noteItemId: Long,
    val key: String,
    val value: String
)