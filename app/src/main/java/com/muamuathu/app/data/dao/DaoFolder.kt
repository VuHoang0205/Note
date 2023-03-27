package com.muamuathu.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.muamuathu.app.data.entity.EntityFolder
import com.muamuathu.app.data.entity.embedded.EntityFolderInfo
import kotlinx.coroutines.flow.Flow


@Dao
abstract class DaoFolder : DaoBase<EntityFolder>() {
    @Transaction
    @Query("SELECT * FROM EntityFolder ORDER BY folderId DESC ")
    abstract fun getFolders(): Flow<List<EntityFolderInfo>>
}