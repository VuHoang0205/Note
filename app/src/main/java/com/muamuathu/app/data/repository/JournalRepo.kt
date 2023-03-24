package com.muamuathu.app.data.repository

import com.muamuathu.app.data.entity.EntityTag
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.presentation.helper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface JournalRepo {
    suspend fun loadFolders(): Flow<List<Folder>>
    suspend fun saveFolder(folder: Folder): ResultWrapper<Unit>
    suspend fun loadTags(): Flow<List<EntityTag>>
    suspend fun addTag(entityTag: EntityTag)
    suspend fun updateFolder(folder: Folder): ResultWrapper<Unit>
    suspend fun deleteFolder(folder: Folder)
}