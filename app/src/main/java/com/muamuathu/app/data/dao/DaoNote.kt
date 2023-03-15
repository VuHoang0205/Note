package com.muamuathu.app.data.dao

import androidx.room.Dao
import com.muamuathu.app.data.entity.Note

@Dao
abstract class DaoNote : DaoBase<Note>() {
}