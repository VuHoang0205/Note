package com.muamuathu.app.domain.mapper

import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.data.entity.embedded.EmbeddedNote
import com.muamuathu.app.domain.model.Note

fun EmbeddedNote.toDomainModel() = Note(
    noteId = note.noteId,
    folderId = note.folderId,
    title = note.title,
    content = note.content,
    avatar = note.avatar,
    tag = note.tag,
    dateTime = note.dateTime,
    attachments = note.attachments,
    tasks = tasks.map { it.toDomainModel() },
)

fun EntityNote.toDomainModel() = Note(
    noteId = noteId,
    folderId = folderId,
    title = title,
    content = content,
    avatar = avatar,
    tag = tag,
    dateTime = dateTime,
    attachments = attachments,
)

fun Note.toEntityModel() = EntityNote(
    noteId = noteId,
    folderId = folderId,
    title = title,
    content = content,
    avatar = avatar,
    tag = tag,
    dateTime = dateTime,
    attachments = attachments
)