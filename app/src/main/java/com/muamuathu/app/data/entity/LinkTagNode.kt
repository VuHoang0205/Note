package com.solid.journal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LinkTagNode(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tagId: Long,
    val noteId: Long
)