package com.muamuathu.app.domain.model


data class Folder(
    val folderId: Long = 0,
    var name: String = "",
    var color: Long = 0L,
    val noteList: List<Note> = emptyList(),
) {
    fun getTotalTask(): Int {
        return noteList.sumOf { it.tasks.size }
    }
}