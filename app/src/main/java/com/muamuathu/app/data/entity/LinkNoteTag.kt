package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("id", unique = true)], foreignKeys = [
    ForeignKey(
        entity = EntityNote::class,
        parentColumns = arrayOf("noteId"),
        childColumns = arrayOf("noteId"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = EntityTag::class,
        parentColumns = arrayOf("tagId"),
        childColumns = arrayOf("tagId"),
        onDelete = ForeignKey.CASCADE
    )
])
data class LinkNoteTag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val tagId: Long,
    val noteId: Long,
)