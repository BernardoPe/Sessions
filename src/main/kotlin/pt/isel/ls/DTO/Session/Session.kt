package pt.isel.ls.DTO.Session

import kotlinx.serialization.Serializable
import pt.isel.ls.DTO.Game.Game
import pt.isel.ls.DTO.Player.Player

const val SESSION_MAX_CAPACITY = 100

/**
 *  Session
 *
 *  The [Session] Data Class is the representation of a Game in the system.
 *
 *  @param sid The session identifier
 *  @param capacity The session capacity
 *  @param date The session date
 */
@Serializable
data class Session(
    val sid: Int,
    val capacity: Int,
    val date: String,
    val gameSession: Game,
    val playersSession: List<Player>
) {
    init {
        require(sid >= 0) { "Session identifier must be a positive number" }
        require(capacity > 1) { "Session capacity must be a positive number" }
        require(capacity <= SESSION_MAX_CAPACITY) { "Session capacity must be less than or equal to $SESSION_MAX_CAPACITY" }
        // require(gameSession.gid > 0) { "Game identifier must be a positive number" }
    }
}
