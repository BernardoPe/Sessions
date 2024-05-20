package pt.isel.ls.data.dto

import kotlinx.serialization.Serializable

/**
 * The [PlayerCreationInputModel] class is used to represent the request body of player creation
 *
 * @param name The player name
 * @param email The player email
 *
 */
@Serializable
data class PlayerCreationInputModel(
    val name: String,
    val email: String,
    val password: String
)

/**
 * The [PlayerCreationOutputModel] class is used to represent the response body of player creation
 *
 * @param pid The player identifier
 * @param token The player token in UUID type
 *
 */
@Serializable
data class PlayerCreationOutputModel(
    val pid: UInt,
    val token: String,
)

/**
 * The [PlayerLoginInputModel] class is used to represent the request body of player login
 *
 * @param name The player name
 * @param email The player email
 * @param password The player password
 *
 */
//@Serializable
//data class PlayerLoginInputModel(
//    val name: String,
//    val email: String,
//    val password: String
//)

/**
 * The [PlayerInfoOutputModel] class is used to represent the response body of the player details
 *
 * @param pid The player identifier
 * @param name The player name
 * @param email The player email
 *
 */
@Serializable
data class PlayerInfoOutputModel(
    val pid: UInt,
    val name: String,
    val email: String
)

/**
 * The [PlayerSearchOutputModel] class is used to represent the response body of the player list
 * @param players The list of players
 * @param total The total number of players
 */
@Serializable
data class PlayerSearchOutputModel(
    val players: List<PlayerInfoOutputModel>,
    val total: Int
)