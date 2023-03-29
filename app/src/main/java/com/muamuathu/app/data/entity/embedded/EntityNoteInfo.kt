package com.muamuathu.app.data.entity.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.data.entity.EntityTag
import com.muamuathu.app.data.entity.LinkNoteTag

data class EntityNoteInfo(
    @Embedded
    val note: EntityNote,

    @Relation(
        parentColumn = "noteId",
        entityColumn = "tagId",
        associateBy = Junction(LinkNoteTag::class)
    )
    val tags: List<EntityTag> = emptyList()
)