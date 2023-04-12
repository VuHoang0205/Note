package com.muamuathu.app.data.base

import android.os.SystemClock
import com.muamuathu.app.data.entity.EntityFolder
import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.data.entity.EntityTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.*
import java.util.concurrent.TimeUnit

object MockData {
    suspend fun getCalendarList(
        zonedDateTime: ZonedDateTime,
    ): Flow<MutableList<ZonedDateTime>> {
        return flow {
            val calendarList = mutableListOf<ZonedDateTime>()
            val endOfMonth = zonedDateTime.withMonth(zonedDateTime.month.value).with(TemporalAdjusters.lastDayOfMonth())
            for (i in 1..endOfMonth.dayOfMonth) {
                val dateTime = zonedDateTime.withMonth(zonedDateTime.month.value).withDayOfMonth(i)
                calendarList.add(dateTime)
            }
            emit(calendarList)
        }
    }

    suspend fun getNote(): Flow<EntityNote> {
        return flow {
            emit(EntityNote(
                1,
                "Fun day with Friends $1",
                "Come on, people now Smile on your bro everybody get together to try new Come on, people now Smile on your bro everybody get together to try new Come on, people now Smile on your bro everybody get together to try new...",
                SystemClock.currentThreadTimeMillis(),
                attachments = listOf("https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg",
                    "https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg",
                    "https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg")
            ))
        }
    }

    suspend fun getMockFolderList(): Flow<MutableList<EntityFolder>> {
        return flow {
            val entityFolderList = mutableListOf<EntityFolder>()
            val random = Random()
            (0..20).toList().forEach {
                entityFolderList.add(EntityFolder(
                    id = random.nextLong(),
                    name = "$it+Fun day with Friends",
                    color = 1
                ))
            }
            emit(entityFolderList)
        }
    }

    suspend fun getMockTaskList(): Flow<MutableList<EntityTask>> {
        return flow {
            val entityTaskList = mutableListOf<EntityTask>()
            val random = Random()
            var time = SystemClock.currentThreadTimeMillis()
            (0..17).toList().forEach {
                time += TimeUnit.HOURS.toMillis(1)
                if (it % 5 == 0) "" else "https://hc.com.vn/i/ecommerce/media/ckeditor_3087086.jpg"
                entityTaskList.add(EntityTask(
                    random.nextInt(Int.MAX_VALUE - 1).toLong(),
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
            emit(entityTaskList)
        }
    }
}

