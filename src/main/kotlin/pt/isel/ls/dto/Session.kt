package pt.isel.ls.dto

import kotlinx.serialization.Serializable


/**
 * The [SessionAddPlayerOutputModel] class is used to represent the response body of an added player to the session
 * The request to add the player to the session is going to need:
 * an session id URI path variable and another URI path variable id of the added player
 * e.g. /api/session/{sid}/{pid}
 *
 *  @param message The message when an player was added to the session.
 *  E.g: "The player <player name> has entered the game session".
 *
 */
@Serializable
data class SessionAddPlayerOutputModel(val message: String)

/**
 * The [SessionCreationInputModel] class is used to represent the request body of a created session
 * @param capacity The session capacity
 * @param date The session date
 * @param gid The game identifier
 */
@Serializable
data class SessionCreationInputModel(val capacity: Int, val date: String, val gid: Int)

/**
 * The [SessionCreationOutputModel] class is used to represent the response body of a created session
 * @param sid The session identifier
 */

@Serializable
data class SessionCreationOutputModel(
    val sid: Int
    /** or UUID */
)

/**
 * The [SessionInfoOutputModel] class is used to represent the response body of the player details
 *
 * @param sid The session identifier
 * @param capacity The number of players the session can hold
 * @param date The date when the session was created
 * @param gameSession The current game session details
 * @param playersSession The list of players details
 *
 */

@Serializable
data class SessionInfoOutputModel(
    val sid: Int,
    val capacity: Int,
    val date: String,
    val gameSession: GameInfoOutputModel,
    val playersSession: Set<PlayerInfoOutputModel>
)

/**
 * The [SessionSearchInputModel] class is used to represent the request body of a session list search
 * @param gid The game identifier of the current session
 * @param date The date when the session was created
 * @param state The state of the session if it can add more players or not
 * @param pid The player identifier
 */
@Serializable
data class SessionSearchInputModel(
    val gid: Int,
    val date: String? = null,
    val state: String? = null,
    val pid: Int? = null
)

/**
 * The [SessionSearchOutputModel] class is used to represent the response body of a session list search
 * @param sessions The list of sessions
 */

@Serializable
data class SessionSearchOutputModel(val sessions: Set<SessionInfoOutputModel>)