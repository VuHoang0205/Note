package com.muamuathu.app.domain.mapper

import com.muamuathu.app.data.entity.EntityFolder
import com.muamuathu.app.data.entity.embedded.EmbeddedFolder
import com.muamuathu.app.domain.model.Folder

fun EmbeddedFolder.toDomainModel() = Folder(
    folderId = folder.folderId,
    name = folder.name,
    color = folder.color,
    noteList = notes.map { it.note.toDomainModel() },
)

fun EntityFolder.toDomainModel() = Folder(
    folderId = folderId,
    name = name,
    color = color,
)

fun Folder.toEntityModel() = EntityFolder(
    folderId = folderId,
    name = name,
    color = color
)