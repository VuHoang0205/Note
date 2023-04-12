package com.muamuathu.app.domain.mapper

import com.muamuathu.app.data.entity.EntityTask
import com.muamuathu.app.domain.model.Task

fun EntityTask.toDomainModel() = Task(
    taskId = id,
    folderId = noteId,
    name = name,
    description = description,
    startDate = startDate,
    reminderTime = reminderTime,
    reminderType = reminderType,
    reminderFrequent = reminderFrequent,
    priority = priority
)

fun Task.toEntityModel() = EntityTask(
    id = taskId,
    noteId = folderId,
    name = name,
    description = description,
    startDate = startDate,
    reminderTime = reminderTime,
    reminderType = reminderType,
    reminderFrequent = reminderFrequent,
    priority = priority
)