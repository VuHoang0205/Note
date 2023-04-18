package com.muamuathu.app.data.dao

import androidx.room.*
import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.data.entity.LinkFolderNote
import com.muamuathu.app.data.entity.LinkNoteTag
import com.muamuathu.app.data.entity.embedded.EntityNoteInfo
import com.muamuathu.app.domain.mapper.toEntityModel
import com.muamuathu.app.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DaoNote : DaoBase<EntityNote>() {
    @Transaction
    @Query("SELECT * FROM EntityNote WHERE startOfDay = :time ORDER BY id DESC ")
    abstract fun getNotes(time: Long): Flow<List<EntityNoteInfo>>

    @Query("SELECT MAX(id) FROM EntityNote")
    abstract fun getMaxId(): Long

    @Transaction
    open suspend fun saveNote(note: Note): Long {
        val idNote = getMaxId() + 1
        if (note.tags.isNotEmpty()) {
            val linkNoteTags = note.tags.map { LinkNoteTag(tagId = it.tagId, noteId = idNote) }
            insertLinkNoteTag(linkNoteTags)
        }
        if (note.folder.name.isNotEmpty()) {
            insertLinkFolderNote(LinkFolderNote(folderId = note.folder.folderId, noteId = idNote))
        }
        note.noteId = idNote
        insert(note.toEntityModel())
        return idNote
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLinkFolderNote(linkFolderNote: LinkFolderNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLinkNoteTag(linkNoteTags: List<LinkNoteTag>)

    @Transaction
    @Query("SELECT * FROM EntityNote WHERE id = :id ORDER BY id DESC ")
    abstract fun getNoteByID(id: Long): EntityNoteInfo
}