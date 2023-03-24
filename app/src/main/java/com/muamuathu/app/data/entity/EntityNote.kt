package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("folderId"), Index("noteId", unique = true)], foreignKeys = [
        ForeignKey(
            entity = EntityFolder::class,
            parentColumns = arrayOf("folderId"),
            childColumns = arrayOf("folderId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntityNote(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long,
    val folderId: Long,
    val title: String,
    val content: String,
    val avatar: String,
    val tag: String,
    val dateTime: Long,
    val attachments: List<String> = emptyList(),
)