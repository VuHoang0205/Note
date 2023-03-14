package com.solid.journal.data.repository

import com.solid.journal.data.JournalDatabase
import com.solid.journal.data.entity.Folder
import com.solid.journal.data.entity.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JournalRepoImpl @Inject constructor(private val database: JournalDatabase) : JournalRepo {
    override fun loadFolders(): Flow<List<Folder>> {
        return database.loadSelectFolders()
    }

    override suspend fun addFolder(folder: Folder) {
        database.daoFolder().insert(folder)
    }

    override fun loadTags(): Flow<List<Tag>> {
        return database.loadTags()
    }

    override suspend fun addTag(tag: Tag) {
        return  database.daoTag().insert(tag)
    }
}