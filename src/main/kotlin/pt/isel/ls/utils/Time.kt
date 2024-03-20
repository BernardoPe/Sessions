package pt.isel.ls.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun String.toLocalDateTime(): LocalDateTime {
    return try {
        LocalDateTime.parse(this)
    } catch (e: Exception) {
        throw IllegalArgumentException("Invalid date format")
    }
}


fun currentLocalTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.isBefore(other: LocalDateTime) = this < other

fun LocalDateTime.isAfter(other: LocalDateTime) = this > other