package pt.isel.ls.data.domain.session

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.isBefore
import java.util.*

const val SESSION_MAX_CAPACITY = 100u

/**
 *  Session
 *
 *  The [Session] Data Class is the representation of a Game in the system.
 *
 *  @param id The session identifier (unique [UInt] number)
 *  @param capacity The session capacity ([UInt] number)
 *  @param date The session date ([LocalDateTime] object)
 *  @param gameSession The session game ([Game] object)
 *  @param playersSession The session players ([Set] of [Player] objects)
 *  @property state The session state ([State] object)
 *
 *  @throws IllegalArgumentException If the session capacity is less than 1 or more than [SESSION_MAX_CAPACITY]
 *  @throws IllegalArgumentException If the session players are more than the capacity
 */
data class Session(
    val id: UInt,
    val capacity: UInt,
    val date: LocalDateTime,
    val gameSession: Game,
    val playersSession: Set<Player>,
) {
    val state: State
        get() = if (playersSession.size.toUInt() == capacity || date.isBefore(currentLocalTime())) State.CLOSE else State.OPEN
    init {
        require(capacity in (1u..SESSION_MAX_CAPACITY)) { "Session capacity must be at least 1 and at most $SESSION_MAX_CAPACITY" }
        require(playersSession.size.toUInt() <= capacity) { "Session players must be less than or equal to capacity" }
    }
}

/**
 *  State
 *
 *  The [State] Enum Class is the representation of a Session state in the system.
 *
 *  @property OPEN The session is open
 *  @property CLOSE The session is closed
 */
enum class State {
    OPEN,
    CLOSE,
    ;

    override fun toString(): String {
        return when (this) {
            OPEN -> "OPEN"
            CLOSE -> "CLOSE"
        }
    }
}

/**
 *  toState
 *
 *  The [toState] function is responsible for converting a [String] to a [State] object.
 *
 *  @return The [State] object
 *  @throws IllegalArgumentException If the state is invalid
 */
fun String.toState(): State {
    return when (this.uppercase(Locale.getDefault())) {
        "OPEN" -> State.OPEN
        "CLOSE" -> State.CLOSE
        else -> throw IllegalArgumentException("Invalid state")
    }
}
