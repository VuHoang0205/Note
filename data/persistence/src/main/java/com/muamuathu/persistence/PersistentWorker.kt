package com.muamuathu.persistence

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class PersistentWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val persistent: Persistence
) : CoroutineWorker(context, workerParameters) {

    companion object {
        private const val KEY = "key"
        private const val VALUE_INT = "value_int"
        private const val VALUE_FLOAT = "value_float"
        private const val VALUE_BOOLEAN = "value_boolean"
        private const val VALUE_STRING = "value_string"
        private const val VALUE_SET_STRING = "value_set_string"

        fun execute(context: Context, key: String, value: Boolean) {
            val data = Data.Builder()
                .putString(KEY, key)
                .putBoolean(VALUE_BOOLEAN, value)
                .build()
            execute(context, key, data)
        }

        fun execute(context: Context, key: String, value: Int) {
            val data = Data.Builder()
                .putString(KEY, key)
                .putInt(VALUE_INT, value)
                .build()
            execute(context, key, data)
        }

        fun execute(context: Context, key: String, value: Float) {
            val data = Data.Builder()
                .putString(KEY, key)
                .putFloat(VALUE_FLOAT, value)
                .build()
            execute(context, key, data)
        }

        fun execute(context: Context, key: String, value: String) {
            val data = Data.Builder()
                .putString(KEY, key)
                .putString(VALUE_STRING, value)
                .build()
            execute(context, key, data)
        }

        fun execute(context: Context, key: String, value: Set<String>) {
            val data = Data.Builder()
                .putString(KEY, key)
                .putStringArray(VALUE_SET_STRING, value.toTypedArray())
                .build()
            execute(context, key, data)
        }

        private fun execute(context: Context, key: String, data: Data) {
            val request = OneTimeWorkRequest.Builder(PersistentWorker::class.java)
                .setInputData(data)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "persistent_$key",
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val key = inputData.getString(KEY) ?: return@withContext Result.failure()

        when {
            inputData.hasKeyWithValueOfType<Boolean>(VALUE_BOOLEAN) -> {
                val value = inputData.getBoolean(VALUE_BOOLEAN, false)
                persistent.save(key, value)
            }
            inputData.hasKeyWithValueOfType<Int>(VALUE_INT) -> {
                val value = inputData.getInt(VALUE_INT, 0)
                persistent.save(key, value)
            }
            inputData.hasKeyWithValueOfType<Float>(VALUE_FLOAT) -> {
                val value = inputData.getFloat(VALUE_FLOAT, 0f)
                persistent.save(key, value)
            }
            inputData.hasKeyWithValueOfType<String>(VALUE_STRING) -> {
                val value = inputData.getString(VALUE_STRING).orEmpty()
                persistent.save(key, value)
            }
            inputData.hasKeyWithValueOfType<Set<String>>(VALUE_SET_STRING) -> {
                val value = inputData.getStringArray(VALUE_SET_STRING)
                persistent.save(key, value.orEmpty().toSet())
            }
        }

        return@withContext Result.success()
    }
}