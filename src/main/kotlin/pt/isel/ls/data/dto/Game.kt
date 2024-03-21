package pt.isel.ls.dto

import kotlinx.serialization.Serializable

/**
 * The [GameCreationInputModel] class is used to represent the request body of the created game
 *
 *
 * @param name The game name
 * @param developer The game developer
 * @param genres The list of genres
 *
 *
 */
@Serializable
data class GameCreationInputModel(val name: String, val developer: String, val genres: Set<String>)

/**
 * The [GameCreationOutputModel] class is used to represent the response body of the created game
 *
 * @param gid The game identifier
 *
 */

@Serializable
data class GameCreationOutputModel(val gid: UInt)

/**
 * The [GameInfoOutputModel] class is used to represent the response body of the game details
 *
 * @param gid The game identifier
 * @param name The game name
 * @param email The developer name
 * @param genres The list of game genres
 *
 */

@Serializable
data class GameInfoOutputModel(val gid: UInt, val name: String, val developer: String, val genres: List<String>)


/**
 * The [GameSearchOutputModel] class is used to represent the response body of the game search
 *
 * @param games The list of game details
 *
 */

@Serializable
data class GameSearchOutputModel(val games: List<GameInfoOutputModel>)