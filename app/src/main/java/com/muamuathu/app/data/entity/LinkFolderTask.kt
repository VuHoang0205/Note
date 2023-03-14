package com.solid.journal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LinkFolderTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val folderId: Long,
    val taskId: Long
)