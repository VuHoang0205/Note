package com.solid.journal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    val accountId: Long = 0,
    val name: String,
    val imageUrl: String
)