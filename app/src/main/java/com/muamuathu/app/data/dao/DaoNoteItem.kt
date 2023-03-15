package com.muamuathu.app.data.dao

import androidx.room.Dao
import com.muamuathu.app.data.entity.NoteItem

@Dao
abstract class DaoNoteItem : DaoBase<NoteItem>() {
}