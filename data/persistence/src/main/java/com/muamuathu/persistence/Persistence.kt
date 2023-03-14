package com.muamuathu.persistence

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Persistence @Inject constructor(@ApplicationContext val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "app_setting.ds")

    suspend fun save(key: String, value: Boolean) {
        context.dataStore.edit { settings ->
            val prefKey = booleanPreferencesKey(key)
            settings[prefKey] = value
        }
    }

    suspend fun save(key: String, value: Int) {
        context.dataStore.edit { settings ->
            val prefKey = intPreferencesKey(key)
            settings[prefKey] = value
        }
    }

    suspend fun save(key: String, value: Float) {
        context.dataStore.edit { settings ->
            val prefKey = floatPreferencesKey(key)
            settings[prefKey] = value
        }
    }

    suspend fun save(key: String, value: Long) {
        context.dataStore.edit { settings ->
            val prefKey = longPreferencesKey(key)
            settings[prefKey] = value
        }
    }

    suspend fun save(key: String, value: String) {
        context.dataStore.edit { settings ->
            val prefKey = stringPreferencesKey(key)
            settings[prefKey] = value
        }
    }

    suspend fun save(key: String, value: Set<String>) {
        context.dataStore.edit { settings ->
            val prefKey = stringSetPreferencesKey(key)
            settings[prefKey] = value
        }
    }

    fun read(key: String, defaultValue: Boolean): Flow<Boolean> {
        val prefKey = booleanPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] ?: defaultValue }
    }

    fun read(key: String, defaultValue: Int): Flow<Int> {
        val prefKey = intPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] ?: defaultValue }
    }

    fun read(key: String, defaultValue: Float): Flow<Float> {
        val prefKey = floatPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] ?: defaultValue }
    }

    fun read(key: String, defaultValue: Long): Flow<Long> {
        val prefKey = longPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] ?: defaultValue }
    }

    fun read(key: String, defaultValue: String): Flow<String> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] ?: defaultValue }
    }

    fun read(key: String, defaultValue: Set<String>): Flow<Set<String>> {
        val prefKey = stringSetPreferencesKey(key)
        return context.dataStore.data.map { it[prefKey] ?: defaultValue }
    }

    fun blockingRead(key: String, defaultValue: Int): Int {
        val prefKey = intPreferencesKey(key)
        return runBlocking {
            context.dataStore.data.map { it[prefKey] ?: defaultValue }.first()
        }
    }
}
