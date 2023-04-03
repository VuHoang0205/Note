package com.muamuathu.app.data.entity.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.muamuathu.app.data.entity.*

data class EntityFolderInfo(
    @Embedded
    val folder: EntityFolder,

    @Relation(
        parentColumn = "folderId",
        entityColumn = "noteId",
        associateBy = Junction(LinkFolderNote::class)
    )
    val notes: List<EntityNote>,

    @Relation(
        parentColumn = "folderId",
        entityColumn = "taskId",
        associateBy = Junction(LinkFolderTask::class)
    )
    val task: List<EntityTask>,
)