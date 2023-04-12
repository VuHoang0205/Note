package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["folderId", "taskId"], indices = [Index("folderId", "taskId", unique = true)])
data class LinkFolderTask(
    val folderId: Long,
    val taskId: Long,
)