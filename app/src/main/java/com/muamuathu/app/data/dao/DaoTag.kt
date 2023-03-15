package com.muamuathu.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.muamuathu.app.data.entity.Tag
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DaoTag : DaoBase<Tag>() {
    @Query("SELECT * FROM tag ORDER BY tagId DESC ")
    abstract fun getTags(): Flow<List<Tag>>
}