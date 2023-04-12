package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["folderId", "noteId"], indices = [Index("folderId", "noteId", unique = true)])
data class LinkFolderNote(
    val folderId: Long,
    val noteId: Long,
)