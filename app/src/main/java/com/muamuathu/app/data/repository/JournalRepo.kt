package com.muamuathu.app.data.repository

import com.muamuathu.app.data.entity.Folder
import com.muamuathu.app.data.entity.Tag
import kotlinx.coroutines.flow.Flow

interface JournalRepo {
    suspend fun loadFolders(): Flow<List<Folder>>
    suspend fun addFolder(folder: Folder)
    suspend fun loadTags(): Flow<List<Tag>>
    suspend fun addTag(tag: Tag)
}