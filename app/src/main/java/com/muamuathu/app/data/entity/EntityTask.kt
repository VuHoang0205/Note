package com.muamuathu.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.muamuathu.app.R

@Entity(
    indices = [Index("noteId"), Index("taskId", unique = true)],
    foreignKeys = [ForeignKey(
        entity = EntityNote::class,
        parentColumns = ["noteId"],
        childColumns = ["noteId"],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class EntityTask(
    @PrimaryKey(autoGenerate = true)
    val taskId: Long,
    val noteId: Long,
    val name: String,
    val description: String,
    val startDate: Long,
    val reminderTime: Long,
    val reminderType: Int,
    val reminderFrequent: Int,
    val priority: Int,
) {
    fun getPriorityTask(): PriorityTask {
        return when (priority) {
            PriorityTask.PriorityNone.priority -> PriorityTask.PriorityNone
            PriorityTask.PriorityLow.priority -> PriorityTask.PriorityLow
            PriorityTask.PriorityNormal.priority -> PriorityTask.PriorityNormal
            PriorityTask.PriorityHigh.priority -> PriorityTask.PriorityHigh
            else -> {
                PriorityTask.PriorityNone
            }
        }
    }
}

sealed class PriorityTask(val priority: Int, val name: String, val color: Int) {
    object PriorityNone : PriorityTask(0, "None", R.color.gulf_blue)
    object PriorityLow : PriorityTask(1, "Low", R.color.orange)
    object PriorityNormal : PriorityTask(2, "Medium", R.color.pumpkin)
    object PriorityHigh : PriorityTask(3, "High", R.color.cinnabar)
}