package com.muamuathu.app.presentation.extensions

import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.text.SimpleDateFormat
import java.util.*

fun Long.toDayMonth(): String {
    val calendar = Calendar.getInstance().clone() as Calendar
    calendar.timeInMillis = this
    return SimpleDateFormat("dd MMMM", Locale.getDefault()).format(calendar.time)
}

fun Long.toHour(): String {
    val calendar = Calendar.getInstance().clone() as Calendar
    calendar.timeInMillis = this
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
}

fun Long.toMonth(): String {
    val calendar = Calendar.getInstance().clone() as Calendar
    calendar.timeInMillis = this
    return SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.time)
}

fun Long.toYearValue(): Int {
    val calendar = Calendar.getInstance().clone() as Calendar
    calendar.timeInMillis = this
    return calendar.get(Calendar.YEAR)
}

fun Long.toDayOfWeekDetail(): String {
    val calendar = Calendar.getInstance().clone() as Calendar
    calendar.timeInMillis = this
    return SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
}

fun ZonedDateTime.toDayOfWeek(): String {
    return format(DateTimeFormatter.ofPattern("E", Locale.getDefault()))
}


fun ZonedDateTime.toDayOfMonth(): String {
    return format(DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault()))
}

fun Long.formatFromPattern(pattern: String): String {
    return try {
        SimpleDateFormat(pattern, Locale.getDefault()).format(this)
    } catch (e: Exception) {
        ""
    }
}

fun List<ZonedDateTime>.indexOfDate(zonedDateTime: ZonedDateTime): Int {
    if (isNotEmpty()) {
        forEachIndexed { index, item ->
            if (item.isSameDay(zonedDateTime)) {
                return index
            }
        }
    }
    return -1
}

fun ZonedDateTime.isSameDay(zonedDateTime: ZonedDateTime): Boolean {
    return truncatedTo(ChronoUnit.DAYS).equals(zonedDateTime.truncatedTo(ChronoUnit.DAYS))
}

fun Long.getStartOfDay(): Long {
    val local = this + TimeZone.getDefault().rawOffset
    return this - (local % 86_400_000L)
}

const val FORMAT_TIME_PICKER="hh:mm:ss"
fun String.toTimeInMillis(format: String): Long {
    return try {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = sdf.parse(this)
        date?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}




