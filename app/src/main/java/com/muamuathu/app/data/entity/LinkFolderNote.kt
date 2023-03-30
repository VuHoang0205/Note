package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("id", unique = true)], foreignKeys = [
    ForeignKey(
        entity = EntityNote::class,
        parentColumns = ["noteId"],
        childColumns = ["noteId"],
    ),
    ForeignKey(
        entity = EntityFolder::class,
        parentColumns = ["folderId"],
        childColumns = ["folderId"],
    ),
])
data class LinkFolderNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val folderId: Long,
    val noteId: Long,
)