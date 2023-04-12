package com.muamuathu.app.domain.mapper

import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.data.entity.embedded.EntityNoteInfo
import com.muamuathu.app.domain.model.Note

fun EntityNoteInfo.toDomainModel() = Note(
    noteId = note.id,
    title = note.title,
    content = note.content,
    dateTime = note.dateTime,
    attachments = note.attachments,
    tags = tags.map { it.toDomainModel() },
    folder = folder.toDomainModel()
)

fun EntityNote.toDomainModel() = Note(
    noteId = id,
    title = title,
    content = content,
    dateTime = dateTime,
    attachments = attachments,
)

fun Note.toEntityModel() = EntityNote(
    id = noteId,
    title = title,
    content = content,
    dateTime = dateTime,
    attachments = attachments
)