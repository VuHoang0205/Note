package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("id", unique = true)], foreignKeys = [
    ForeignKey(
        entity = EntityFolder::class,
        parentColumns = arrayOf("folderId"),
        childColumns = arrayOf("folderId"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = EntityTask::class,
        parentColumns = arrayOf("taskId"),
        childColumns = arrayOf("taskId"),
        onDelete = ForeignKey.CASCADE
    )
])
data class LinkFolderTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val folderId: Long,
    val taskId: Long,
)