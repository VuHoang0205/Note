package com.muamuathu.app.data.entity.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.muamuathu.app.data.entity.*

data class EntityNoteInfo(
    @Embedded
    val note: EntityNote,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            LinkNoteTag::class,
            parentColumn = "noteId",
            entityColumn = "tagId",
        ),
    )
    val tags: List<EntityTag>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            LinkFolderNote::class,
            parentColumn = "noteId",
            entityColumn = "folderId",
        )
    )
    val folder: EntityFolder,
)