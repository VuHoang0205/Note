package com.muamuathu.app.data.dao

import androidx.room.Dao
import com.solid.journal.data.entity.Note

@Dao
abstract class DaoNote : DaoBase<Note>() {
}