package com.muamuathu.app.domain.model

data class Note(
    val noteId: Long = 0L,
    val title: String = "",
    val content: String = "",
    val avatar: String = "",
    val dateTime: Long = 0L,
    val attachments: List<String> = mutableListOf<String>().apply { add("https://hc.com.vn/i/ecommerce/media/ckeditor_3087086.jpg") },
)