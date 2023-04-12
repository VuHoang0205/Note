package com.muamuathu.app.domain.mapper

import com.muamuathu.app.data.entity.EntityFolder
import com.muamuathu.app.data.entity.embedded.EntityFolderInfo
import com.muamuathu.app.domain.model.Folder

fun EntityFolderInfo.toDomainModel() = Folder(
    folderId = folder.id,
    name = folder.name,
    color = folder.color,
    noteList = notes.map { it.toDomainModel() },
)

fun EntityFolder.toDomainModel() = Folder(
    folderId = id,
    name = name,
    color = color,
)

fun Folder.toEntityModel() = EntityFolder(
    id = folderId,
    name = name,
    color = color
)