package pt.isel.ls.data.domain.session

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.isAfter
import pt.isel.ls.utils.isBefore
import java.util.*

const val SESSION_MAX_CAPACITY = 100u

/**
 *  Session
 *
 *  The [Session] Data Class is the representation of a Game in the system.
 *
 *  @param id The session identifier
 *  @param capacity The session capacity
 *  @param date The session date
 */
data class Session(
    val id: UInt,
    val capacity: UInt,
    val date: LocalDateTime,
    val gameSession: Game,
    val playersSession: Set<Player>
) {
    val state : State
        get() = if (playersSession.size.toUInt() == capacity || date.isBefore(currentLocalTime())) State.CLOSE else State.OPEN
    init {
        require(capacity in (1u..SESSION_MAX_CAPACITY)) { "Session capacity must be at least 1 and at most $SESSION_MAX_CAPACITY" }
        require(playersSession.size.toUInt() <= capacity) { "Session players must be less than or equal to capacity" }
        //require(date.isAfter(currentLocalTime())) { "Session date must be in the future" } makes it impossible to get closed sessions from the database, validate when creating session instead
    }

}

enum class State {
    OPEN,
    CLOSE;
    override fun toString(): String {
        return when (this) {
            OPEN -> "OPEN"
            CLOSE -> "CLOSE"
        }
    }

}

fun String.toState(): State {
    return when (this.uppercase(Locale.getDefault())) {
        "OPEN" -> State.OPEN
        "CLOSE" -> State.CLOSE
        else -> throw IllegalArgumentException("Invalid state")
    }
}

