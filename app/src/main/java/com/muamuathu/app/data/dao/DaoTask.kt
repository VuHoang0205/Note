package com.muamuathu.app.data.dao

import androidx.room.Dao
import com.muamuathu.app.data.entity.Task

@Dao
abstract class DaoTask : DaoBase<Task>() {
}