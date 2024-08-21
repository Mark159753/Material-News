package com.develop.common.date

import android.text.format.DateUtils
import android.util.Log
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.util.Date

fun String.relativeFormatDate():String{
    if (this.isBlank()) return ""
    val date = toDate() ?: return ""
    val time = date.time
    val now = System.currentTimeMillis()
    val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
    return ago.toString()
}

fun Date.relativeFormatDate():String{
    val now = System.currentTimeMillis()
    val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
    return ago.toString()
}

fun String.toDate(): Date?{
    if (this.isBlank()) return null
    return try {
        val instant = Instant.parse(this)
        instant.atZone(ZoneId.systemDefault())
        Date.from(instant)
    }catch (e: DateTimeParseException){
        Log.e("toDate", e.stackTraceToString())
        null
    }
}