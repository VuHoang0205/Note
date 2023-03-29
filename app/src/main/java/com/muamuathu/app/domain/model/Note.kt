package com.muamuathu.app.domain.model

data class Note(
    val noteId: Long = 0L,
    val title: String = "",
    val content: String = "",
    val dateTime: Long = 0L,
    val attachments: List<String> = emptyList(),
    val tags: List<Tag> = emptyList(),
)