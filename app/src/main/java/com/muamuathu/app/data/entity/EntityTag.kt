package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityTag(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
)