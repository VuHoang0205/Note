package com.muamuathu.app.data.entity.embedded

import androidx.room.Embedded
import androidx.room.Relation
import com.muamuathu.app.data.entity.EntityFolder
import com.muamuathu.app.data.entity.EntityNote

data class EmbeddedFolder(
    @Embedded
    val folder: EntityFolder,

    @Relation(
        parentColumn = "folderId",
        entityColumn = "folderId",
        entity = EntityNote::class
    )
    val notes: List<EmbeddedNote> = emptyList()
)