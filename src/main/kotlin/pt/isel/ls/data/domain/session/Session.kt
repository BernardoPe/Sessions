package pt.isel.ls.data.domain.session

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
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
 *  @param sid The session identifier
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
        get() = if (playersSession.size.toUInt() == capacity && date.isBefore(currentLocalTime())) State.CLOSE else State.OPEN
    init {
        require(capacity in (2u..SESSION_MAX_CAPACITY)) { "Session capacity must be at least 2 and at most $SESSION_MAX_CAPACITY" }
        require(playersSession.size.toUInt() <= capacity) { "Session players must be less than or equal to capacity" }
        require(date.isAfter(currentLocalTime())) { "Session date must be in the future" }
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

    fun from(value: String): State {
        return when (value.uppercase(Locale.getDefault())) {
            "OPEN" -> OPEN
            "CLOSE" -> CLOSE
            else -> throw IllegalArgumentException("Invalid state")
        }
    }

}


