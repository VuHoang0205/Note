package com.muamuathu.app.data.entity.embedded

import androidx.room.Embedded
import androidx.room.Relation
import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.data.entity.EntityTask

data class EmbeddedNote(
    @Embedded
    val note: EntityNote,

    @Relation(
        parentColumn = "noteId",
        entityColumn = "noteId",
        entity = EntityTask::class
    )
    val tasks: List<EntityTask> = emptyList()
)