package com.muamuathu.app.data.repository

import com.muamuathu.app.data.JournalDatabase
import com.muamuathu.app.domain.mapper.toDomainModel
import com.muamuathu.app.domain.mapper.toEntityModel
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.domain.model.SubTask
import com.muamuathu.app.domain.model.Tag
import com.muamuathu.app.presentation.extensions.getStartOfDay
import com.muamuathu.app.presentation.helper.safeDataBaseCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JournalRepoImpl @Inject constructor(private val database: JournalDatabase) : JournalRepo {
    override suspend fun loadNote(time: Long): Flow<List<Note>> {
        return database.daoNote().getNotes(time.getStartOfDay()).map { it.map { it.toDomainModel() } }
    }

    override suspend fun loadFolders(): Flow<List<Folder>> {
        return database.loadFolders().map { it.map { it.toDomainModel() } }
    }

    override suspend fun saveFolder(folder: Folder) = safeDataBaseCall {
        database.daoFolder().insert(folder.toEntityModel())
    }

    override suspend fun loadTags(): Flow<List<Tag>> {
        return database.loadTags().map { it.map { it.toDomainModel() } }
    }

    override suspend fun addTag(tag: Tag) = safeDataBaseCall {
        database.daoTag().insert(tag.toEntityModel())
    }

    override suspend fun updateFolder(folder: Folder) = safeDataBaseCall {
        database.daoFolder().update(folder.toEntityModel())
    }

    override suspend fun deleteFolder(folder: Folder) {
        database.daoFolder().delete(folder.toEntityModel())
    }

    override suspend fun saveNote(note: Note) = safeDataBaseCall {
        database.daoNote().saveNote(note)
    }

    override suspend fun deleteNote(note: Note) = safeDataBaseCall {
        database.daoNote().delete(note.toEntityModel())
    }

    override suspend fun getNoteById(idNote: Long) = safeDataBaseCall {
        database.daoNote().getNoteByID(idNote).toDomainModel()
    }

    override suspend fun getSubTasks(): Flow<List<SubTask>> {
        return database.daoSubTask().getSubTasks().map { it.map { it.toDomainModel() } }
    }

    override suspend fun saveSubTask(subTask: SubTask) = safeDataBaseCall {
        database.daoSubTask().insert(subTask.toEntityModel())
    }
}