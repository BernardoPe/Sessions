package pt.isel.ls.DTO.Session

import kotlinx.serialization.Serializable
import pt.isel.ls.utils.isValidTimeStamp
import java.sql.Timestamp

/**
 * The [SessionRequest] class is used to represent the request body of a session
 *
 * If an empty date or capacity is provided, an exception is thrown
 * @param capacity The session capacity
 * @param date The session date
 * @param gid The game identifier
 */
@Serializable
data class SessionRequest(
    val capacity: Int,
    val date: String,
    val gid: Int,
) {
    init {
        require(capacity > 1) { "Session capacity must be a positive number" }
        require(capacity <= SESSION_MAX_CAPACITY) { "Session capacity must be less than or equal to $SESSION_MAX_CAPACITY" }
        require(date.isNotBlank() && date.isValidTimeStamp()) { "Session date must be a valid date format" }
        require(gid > 0) { "Game identifier must be a positive number" }
    }
}
