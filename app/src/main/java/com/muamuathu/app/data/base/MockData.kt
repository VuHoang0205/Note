package com.muamuathu.app.data.base

import android.os.SystemClock
import com.solid.journal.data.entity.Folder
import com.solid.journal.data.entity.Note
import com.solid.journal.data.entity.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.util.*
import java.util.concurrent.TimeUnit

object MockData {
    suspend fun getCalendarList(
        zonedDateTime: ZonedDateTime,
    ): Flow<MutableList<ZonedDateTime>> {
        return flow {
            val calendarList = mutableListOf<ZonedDateTime>()
            val endOfMonth = zonedDateTime.withMonth(zonedDateTime.month.value).with(
                TemporalAdjusters.lastDayOfMonth())
            for (i in 1..endOfMonth.dayOfMonth) {
                val dateTime = zonedDateTime.withMonth(zonedDateTime.month.value).withDayOfMonth(i)
                calendarList.add(dateTime)
            }
            emit(calendarList)
        }
    }


    suspend fun getNoteItemList(): Flow<MutableList<Note>> {
        return flow {
            val noteList = mutableListOf<Note>()
            val random = Random()
            var time = SystemClock.currentThreadTimeMillis()
            (0..17).toList().forEach {
                time += TimeUnit.HOURS.toMillis(1)
                val avatar =
                    if (it % 5 == 0) "" else "https://hc.com.vn/i/ecommerce/media/ckeditor_3087086.jpg"
                noteList.add(Note(
                    random.nextInt(Int.MAX_VALUE - 1).toLong(),
                    "Fun day with Friends $it",
                    "Come on, people now Smile on your bro everybody get together to try new...",
                    avatar,
                    "Fun,Family",
                    time))
            }
            emit(noteList)
        }
    }

    suspend fun getNote(): Flow<Note> {
        return flow {
            emit(Note(
                1,
                "Fun day with Friends $1",
                "Come on, people now Smile on your bro everybody get together to try new Come on, people now Smile on your bro everybody get together to try new Come on, people now Smile on your bro everybody get together to try new...",
                "https://hc.com.vn/i/ecommerce/media/ckeditor_3087086.jpg,https://hc.com.vn/i/ecommerce/media/ckeditor_3087086.jpg",
                "Fun,Family",
                SystemClock.currentThreadTimeMillis(),
                attachments = listOf("https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg",
                    "https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg",
                    "https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg")
            ))
        }
    }

    suspend fun getMockFolderList(): Flow<MutableList<Folder>> {
        return flow {
            val folderList = mutableListOf<Folder>()
            val random = Random()
            (0..20).toList().forEach {
                folderList.add(Folder(
                    folderId = random.nextLong(),
                    name = "$it+Fun day with Friends",
                    color = 1
                ))
            }
            emit(folderList)
        }
    }

    suspend fun getMockTaskList(): Flow<MutableList<Task>> {
        return flow {
            val taskList = mutableListOf<Task>()
            val random = Random()
            var time = SystemClock.currentThreadTimeMillis()
            (0..17).toList().forEach {
                time += TimeUnit.HOURS.toMillis(1)
                if (it % 5 == 0) "" else "https://hc.com.vn/i/ecommerce/media/ckeditor_3087086.jpg"
                taskList.add(Task(
                    random.nextInt(Int.MAX_VALUE - 1).toLong(),
                    "Metting With James Royal $it",
                    "Come on, people now Smile on your bro everybody get together to try new...",
                    time,
                    0,
                    if (it % 3 == 0) 0 else 1,
                    0,
                    random.nextInt(3)
                ))
            }
            emit(taskList)
        }
    }
}

