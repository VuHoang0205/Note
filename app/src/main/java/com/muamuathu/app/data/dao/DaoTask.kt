package com.muamuathu.app.data.dao

import androidx.room.Dao
import com.solid.journal.data.entity.Task

@Dao
abstract class DaoTask : DaoBase<Task>() {
}