package com.kingsleyadio.appcommons.datetime.util

import java.util.*

/**
 * @author ADIO Kingsley O.
 * @since 19 Jul, 2018
 */

var Calendar.year: Int
    get() = get(Calendar.YEAR)
    set(value) = set(Calendar.YEAR, value)

var Calendar.month: Int
    get() = get(Calendar.MONTH)
    set(value) = set(Calendar.MONTH, value)

var Calendar.dayOfMonth: Int
    get() = get(Calendar.DAY_OF_MONTH)
    set(value) = set(Calendar.DAY_OF_MONTH, value)

var Calendar.hourOfDay: Int
    get() = get(Calendar.HOUR_OF_DAY)
    set(value) = set(Calendar.HOUR_OF_DAY, value)

var Calendar.minute: Int
    get() = get(Calendar.MINUTE)
    set(value) = set(Calendar.MINUTE, value)

var Calendar.second: Int
    get() = get(Calendar.SECOND)
    set(value) = set(Calendar.SECOND, value)

var Calendar.millisecond: Int
    get() = get(Calendar.MILLISECOND)
    set(value) = set(Calendar.MILLISECOND, value)


fun Date.localAsUtc(): Date {
    val offset = TimeZone.getDefault().getOffset(time)
    return Date(time + offset)
}

fun Date.utcAsLocal(): Date {
    val offset = TimeZone.getDefault().getOffset(time)
    return Date(time - offset)
}

fun Date.startOfDay(): Date {
    return Calendar.getInstance().apply {
        time = this@startOfDay
        hourOfDay = 0
        minute = 0
        second = 0
        millisecond = 0

    }.time
}

fun Date.endOfDay(): Date {
    return Calendar.getInstance().apply {
        time = this@endOfDay
        dayOfMonth += 1
        hourOfDay = 0
        minute = 0
        second = 0
        millisecond = -1

    }.time
}

fun Date.startOfMonth(): Date {
    return Calendar.getInstance().apply {
        time = this@startOfMonth
        dayOfMonth = 1
        hourOfDay = 0
        minute = 0
        second = 0
        millisecond = 0

    }.time
}

fun Date.endOfMonth(): Date {
    return Calendar.getInstance().apply {
        time = this@endOfMonth
        month += 1
        dayOfMonth = 1
        hourOfDay = 0
        minute = 0
        second = 0
        millisecond = -1

    }.time
}
