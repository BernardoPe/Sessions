package pt.isel.ls.utils

import kotlinx.datetime.*
import java.sql.Timestamp

fun String.toLocalDateTime(): LocalDateTime {
    return try {
        LocalDateTime.parse(this)
    } catch (e: Exception) {
        throw IllegalArgumentException("Invalid date format")
    }
}

fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = this.toInstant(TimeZone.currentSystemDefault())
    return Timestamp.from(instant.toJavaInstant())
}

fun currentLocalTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.isBefore(other: LocalDateTime) = this < other

fun LocalDateTime.isAfter(other: LocalDateTime) = this > other