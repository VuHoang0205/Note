package com.muamuathu.app.data.entity.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.muamuathu.app.data.entity.*

data class EntityNoteInfo(
    @Embedded
    val note: EntityNote,

    @Relation(
        parentColumn = "noteId",
        entityColumn = "tagId",
        associateBy = Junction(LinkNoteTag::class)
    )
    val tags: List<EntityTag> = emptyList(),

    @Relation(
        parentColumn = "noteId",
        entityColumn = "folderId",
        associateBy = Junction(LinkFolderNote::class)
    )
    val folder: EntityFolder = EntityFolder()

)