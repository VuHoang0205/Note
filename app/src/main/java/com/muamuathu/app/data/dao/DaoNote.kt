package com.muamuathu.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.data.entity.embedded.EntityNoteInfo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DaoNote : DaoBase<EntityNote>() {
    @Transaction
    @Query("SELECT * FROM EntityNote ORDER BY noteId DESC ")
    abstract fun getNotes(): Flow<List<EntityNoteInfo>>
}