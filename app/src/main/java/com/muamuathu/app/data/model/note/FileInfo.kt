package com.solid.journal.data.model.note

import java.util.*

const val DEFAULT_ID = "-1"

data class FileInfo(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val data: String = "",
    val duration: Long = 0,
    val size: Long = 0,
) {
    fun getDurationString(): String {
        if (duration > 0) {
            var min = 0
            var sec: Int = (duration / 1000).toInt()
            if (sec > 60) {
                min = sec / 60
                sec %= 60
            }
            return String.format("%02d:%02d", min, sec)
        }
        return ""
    }
}
