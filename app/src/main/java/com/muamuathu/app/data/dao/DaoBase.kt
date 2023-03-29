package com.muamuathu.app.data.dao

import androidx.room.*

@Dao
abstract class DaoBase<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: List<T>)

    @Update
    abstract suspend fun update(entity: T)

    @Delete
    abstract suspend fun delete(entity: T)

}