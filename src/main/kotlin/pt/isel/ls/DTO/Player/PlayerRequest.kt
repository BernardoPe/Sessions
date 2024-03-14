package pt.isel.ls.DTO.Player

import kotlinx.serialization.Serializable

/**
 * The [PlayerRequest] class is used to represent the request body of a player
 *
 * If an empty e-mail or name is provided, an exception is thrown
 * @param name The player name
 * @param email The player email
 *
 *

 */
@Serializable
data class PlayerRequest (
    val name: String,
    val email: String
) {
    init {
        require(name.isNotBlank()) { "The player name must not be empty" }
        require(email.isNotBlank()) { "The player email must not be empty" }
    }
}