package com.muamuathu.app.domain.mapper

import com.muamuathu.app.data.entity.EntityTask
import com.muamuathu.app.domain.model.Task

fun EntityTask.toDomainModel() = Task(
    taskId = taskId,
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
    taskId = taskId,
    noteId = folderId,
    name = name,
    description = description,
    startDate = startDate,
    reminderTime = reminderTime,
    reminderType = reminderType,
    reminderFrequent = reminderFrequent,
    priority = priority
)