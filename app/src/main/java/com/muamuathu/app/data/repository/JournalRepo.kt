package com.muamuathu.app.data.repository

import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.Tag
import com.muamuathu.app.presentation.helper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface JournalRepo {
    suspend fun loadFolders(): Flow<List<Folder>>
    suspend fun saveFolder(folder: Folder): ResultWrapper<Unit>
    suspend fun loadTags(): Flow<List<Tag>>
    suspend fun addTag(tag: Tag): ResultWrapper<Unit>
    suspend fun updateFolder(folder: Folder): ResultWrapper<Unit>
    suspend fun deleteFolder(folder: Folder)
}