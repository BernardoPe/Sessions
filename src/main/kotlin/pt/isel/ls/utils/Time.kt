package pt.isel.ls.utils

import java.sql.Timestamp

fun String.isValidTimeStamp(): Boolean {
    try {
        Timestamp.valueOf(this)
        return true
    } catch (e: Exception) {
        return false
    }
}

fun String.toTimeStamp(): Timestamp {
    return Timestamp.valueOf(this)
}

fun Timestamp.isGreaterThan(timestamp: Timestamp): Boolean {
    return this.after(timestamp)
}