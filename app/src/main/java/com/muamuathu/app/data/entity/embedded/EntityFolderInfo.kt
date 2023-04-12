package com.muamuathu.app.data.entity.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.muamuathu.app.data.entity.*

data class EntityFolderInfo(
    @Embedded
    val folder: EntityFolder,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            LinkFolderNote::class,
            parentColumn = "folderId",
            entityColumn = "noteId"
        )
    )
    val notes: List<EntityNote>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            LinkFolderTask::class,
            parentColumn = "folderId",
            entityColumn = "taskId",
        )
    )
    val task: List<EntityTask>,
)