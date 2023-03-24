package com.muamuathu.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.muamuathu.app.data.entity.EntityTag
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DaoTag : DaoBase<EntityTag>() {
    @Query("SELECT * FROM entitytag ORDER BY tagId DESC ")
    abstract fun getTags(): Flow<List<EntityTag>>
}