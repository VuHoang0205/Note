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
    @Query("SELECT * FROM EntityNote ORDER BY id DESC ")
    abstract fun getNotes(): List<EntityNoteInfo>

    @Query("select MAX(id) from EntityNote")
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
    @Query("SELECT * FROM EntityNote AS N INNER JOIN LinkFolderNote AS F ON N.id==F.noteId LEFT JOIN LinkNoteTag AS T ON N.id==T.noteId WHERE N.startOfDay = :time GROUP BY N.id ORDER BY id DESC")
    abstract fun getNotes(time: Long): List<EntityNoteInfo>

    @Transaction
    @Query("SELECT * FROM EntityNote AS N INNER JOIN LinkFolderNote AS F ON N.id==F.noteId LEFT JOIN LinkNoteTag AS T ON N.id==T.noteId WHERE N.id = :id  ORDER BY id DESC")
    abstract fun getNoteByID(id: Long): EntityNoteInfo
}