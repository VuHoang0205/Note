package com.muamuathu.app.domain.model

data class Note(
    val noteId: Long,
    val folderId: Long,
    val title: String,
    val content: String,
    val avatar: String,
    val tag: String,
    val dateTime: Long,
    val attachments: List<String> = emptyList(),
    val tasks: List<Task> = emptyList(),
)