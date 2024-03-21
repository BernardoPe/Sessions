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
data class SessionCreationInputModel(val capacity: UInt, val date: String, val gid: UInt)

/**
 * The [SessionCreationOutputModel] class is used to represent the response body of a created session
 * @param sid The session identifier
 */

@Serializable
data class SessionCreationOutputModel(
    val sid: UInt
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
    val sid: UInt,
    val capacity: UInt,
    val date: String,
    val gameSession: GameInfoOutputModel,
    val playersSession: List<PlayerInfoOutputModel>
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
    val gid: UInt,
    val date: String? = null,
    val state: String? = null,
    val pid: UInt? = null
)

/**
 * The [SessionSearchOutputModel] class is used to represent the response body of a session list search
 * @param sessions The list of sessions
 */

@Serializable
data class SessionSearchOutputModel(val sessions: List<SessionInfoOutputModel>)

/**
 * The [SessionAddPlayerInputModel] class is used to represent the request body of adding a player to the session
 * @param pid The player identifier
 */
@Serializable
data class SessionAddPlayerInputModel(val pid: UInt)