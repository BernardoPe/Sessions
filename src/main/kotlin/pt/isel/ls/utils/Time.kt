package pt.isel.ls.utils

import kotlinx.datetime.*
import java.sql.Timestamp
import kotlin.time.Duration


/**
 * Converts a [String] to a [LocalDateTime] instance.
 *
 * @return The [LocalDateTime] instance
 */
fun String.toLocalDateTime(): LocalDateTime {
    return try {
        LocalDateTime.parse(this)
    } catch (e: Exception) {
        throw IllegalArgumentException("Invalid date format")
    }
}

/**
 * Converts a [LocalDateTime] instance to a [Timestamp] instance.
 *
 * Used on the database to store the date.
 *
 * @return The [Timestamp] instance
 */
fun LocalDateTime.toTimestamp(): Timestamp {
    val instant = this.toInstant(TimeZone.currentSystemDefault())
    return Timestamp.from(instant.toJavaInstant())
}

/**
 * Adds a [Duration] to a [LocalDateTime].
 *
 * @param duration The [Duration] to add to the [LocalDateTime]
 * @return The [LocalDateTime] with the added [Duration]
 */
operator fun LocalDateTime.plus(duration: Duration): LocalDateTime {
    val instant = this.toInstant(TimeZone.currentSystemDefault())
    return (instant + duration).toLocalDateTime(TimeZone.currentSystemDefault())
}

/**
 * Gets the current local time.
 *
 * @return The current local time as a [LocalDateTime]
*/
fun currentLocalTime() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

/**
 * Checks if a [LocalDateTime] is before another [LocalDateTime].
 *
 * @param other The [LocalDateTime] to compare
 * @return True if the [LocalDateTime] is before the other [LocalDateTime], false otherwise
 */
fun LocalDateTime.isBefore(other: LocalDateTime) = this < other

/**
 * Checks if a [LocalDateTime] is after another [LocalDateTime].
 *
 * @param other The [LocalDateTime] to compare
 * @return True if the [LocalDateTime] is after the other [LocalDateTime], false otherwise
 */
fun LocalDateTime.isAfter(other: LocalDateTime) = this > other
