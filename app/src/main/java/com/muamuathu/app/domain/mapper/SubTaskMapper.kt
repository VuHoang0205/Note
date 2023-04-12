package com.muamuathu.app.domain.mapper

import com.muamuathu.app.data.entity.EntitySubTask
import com.muamuathu.app.domain.model.SubTask

fun EntitySubTask.toDomainModel() = SubTask(
    subTaskId = id,
    taskId = taskId,
    name = name,
    reminderTime = reminderTime,
    isDone = isDone
)

fun SubTask.toEntityModel() = EntitySubTask(
    id = subTaskId,
    taskId = taskId,
    name = name,
    reminderTime = reminderTime,
    isDone = isDone
)