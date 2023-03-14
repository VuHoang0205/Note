package com.solid.journal.data.repository

import com.solid.journal.data.entity.Folder
import com.solid.journal.data.entity.Tag
import kotlinx.coroutines.flow.Flow

interface JournalRepo {
    fun loadFolders(): Flow<List<Folder>>
    suspend fun addFolder(folder: Folder)
    fun loadTags():Flow<List<Tag>>
    suspend fun addTag(tag: Tag)
}