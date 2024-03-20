package pt.isel.ls.domain.session

import pt.isel.ls.domain.game.Game
import pt.isel.ls.domain.player.Player
import pt.isel.ls.utils.isValidTimeStamp

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
data class Session(
    val sid: Int,
    val capacity: Int,
    val date: String,
    val gameSession: Game,
    val playersSession: Set<Player>
) {
    init {
        /** require(sid >= 0) { "Session identifier must be a positive number" } */ // Unnecessary require
        /** require(capacity > 1) { "Session capacity must be a positive number" } */ // It's used on the service excpetions
        /** require(capacity <= SESSION_MAX_CAPACITY) { "Session capacity must be less than or equal to $SESSION_MAX_CAPACITY" } */ // It's used on the service excpetions
        require(date.isNotBlank() && date.isValidTimeStamp()) { "Session date must be a valid date format" }
    }

    enum class State {
        OPEN,
        CLOSE;

        val isOpen: Boolean
            get() = this == OPEN

        override fun toString(): String {
            return when (this) {
                OPEN -> "OPEN"
                CLOSE -> "CLOSE"
            }
        }
    }
}




