package pt.isel.ls.DTO.Session

/**
 *  Session
 *
 *  The [Session] Data Class is the representation of a Game in the system.
 *
 *  @param ssid The session identifier
 *  @param capacity The session capacity
 *  @param gid The game identifier
 *  @param date The session date
 */
data class Session(
    val ssid: Int,
    val capacity: Int,
    val gid: Int,
    val date: String,
)
