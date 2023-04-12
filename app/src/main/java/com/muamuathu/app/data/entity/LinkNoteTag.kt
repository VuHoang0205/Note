package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["tagId", "noteId"], indices = [Index("tagId", "noteId", unique = true)])
data class LinkNoteTag(
    val tagId: Long,
    val noteId: Long,
)