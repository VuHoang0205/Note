package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityFolder(
    @PrimaryKey(autoGenerate = true)
    val folderId: Long = 0,
    var name: String = "",
    var color: Long = 0,
)