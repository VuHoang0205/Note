package com.muamuathu.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.solid.journal.data.entity.Folder
import kotlinx.coroutines.flow.Flow


@Dao
abstract class DaoFolder : DaoBase<Folder>() {
    @Query("SELECT * FROM folder ORDER BY folderId DESC ")
    abstract fun getFolders(): Flow<List<Folder>>
}