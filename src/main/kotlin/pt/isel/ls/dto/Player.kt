package pt.isel.ls.dto

import kotlinx.serialization.Serializable

/**
 * The [PlayerCreationInputModel] class is used to represent the request body of player creation
 *
 * @param name The player name
 * @param email The player email
 *
 */
@Serializable
data class PlayerCreationInputModel(val name: String, val email: String)

/**
 * The [PlayerCreationOutputModel] class is used to represent the response body of player creation
 *
 * @param pid The player identifier
 * @param token The player token
 *
 */

@Serializable
data class PlayerCreationOutputModel(
    val pid: Int
    /** or UUID */
    , val token: String
)

/**
 * The [PlayerInfoOutputModel] class is used to represent the response body of the player details
 *
 * @param pid The player identifier
 * @param name The player name
 * @param email The player email
 *
 */

@Serializable
data class PlayerInfoOutputModel(val pid: Int, val name: String, val email: String)

