package com.muamuathu.app.data.repository

import com.muamuathu.app.data.JournalDatabase
import com.muamuathu.app.data.entity.LinkFolderNote
import com.muamuathu.app.data.entity.LinkNoteTag
import com.muamuathu.app.domain.mapper.toDomainModel
import com.muamuathu.app.domain.mapper.toEntityModel
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.domain.model.Tag
import com.muamuathu.app.presentation.helper.safeDataBaseCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JournalRepoImpl @Inject constructor(private val database: JournalDatabase) : JournalRepo {
    override suspend fun loadNote(): Flow<List<Note>> {
        return database.loadNotes().map { it.map { it.toDomainModel() } }
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
        val idNote = database.daoNote().insert(note.toEntityModel())
        if (note.tags.isNotEmpty()) {
            val linkNoteTags = note.tags.map { LinkNoteTag(tagId = it.tagId, noteId = idNote) }
            database.daoLinkTagNode().insert(linkNoteTags)
        }
        if (note.folder.name.isNotEmpty()) {
            database.daoLinkFolderNote()
                .insert(LinkFolderNote(folderId = note.folder.folderId, noteId = idNote))
        }
        return@safeDataBaseCall idNote
    }

    override suspend fun deleteNote(note: Note) = safeDataBaseCall {
        database.daoNote().delete(note.toEntityModel())
    }
}