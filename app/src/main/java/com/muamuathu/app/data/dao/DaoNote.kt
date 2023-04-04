package com.muamuathu.app.data.dao

import androidx.room.*
import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.data.entity.LinkFolderNote
import com.muamuathu.app.data.entity.LinkNoteTag
import com.muamuathu.app.data.entity.embedded.EntityNoteInfo
import com.muamuathu.app.domain.mapper.toEntityModel
import com.muamuathu.app.domain.model.Note

@Dao
abstract class DaoNote : DaoBase<EntityNote>() {
    @Transaction
    @Query("SELECT * FROM EntityNote ORDER BY noteId DESC ")
    abstract fun getNotes(): List<EntityNoteInfo>

    @Query("select MAX(noteId) from EntityNote")
    abstract fun getMaxId(): Long

    @Transaction
    open suspend fun saveNote(note: Note): Long {
        val idNote = insert(note.toEntityModel())
        if (note.tags.isNotEmpty()) {
            val linkNoteTags = note.tags.map { LinkNoteTag(tagId = it.tagId, noteId = idNote) }
            insertLinkNoteTag(linkNoteTags)
        }
        if (note.folder.name.isNotEmpty()) {
            insertLinkFolderNote(LinkFolderNote(folderId = note.folder.folderId, noteId = idNote))
        }
        return idNote
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLinkFolderNote(linkFolderNote: LinkFolderNote)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLinkNoteTag(linkNoteTags: List<LinkNoteTag>)

    @Transaction
    @Query("SELECT * FROM EntityNote WHERE startOfDay = :time tim ORDER BY noteId DESC ")
    abstract fun getNotes(time: Long): List<EntityNoteInfo>
}