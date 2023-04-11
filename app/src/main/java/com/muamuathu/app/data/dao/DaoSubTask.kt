package com.muamuathu.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.muamuathu.app.data.entity.EntitySubTask
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DaoSubTask : DaoBase<EntitySubTask>() {
    @Transaction
    @Query("SELECT * FROM EntitySubTask ORDER BY subTaskId DESC ")
    abstract fun getSubTasks(): Flow<List<EntitySubTask>>
}