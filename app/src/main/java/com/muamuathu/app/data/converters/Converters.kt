package com.muamuathu.app.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromString(string: String): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return try {
            Gson().fromJson(string, type)
        } catch (e: Exception) {
            null
        }
    }
}