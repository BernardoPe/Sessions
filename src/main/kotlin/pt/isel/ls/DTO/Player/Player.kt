package pt.isel.ls.DTO.Player

import kotlinx.serialization.Serializable

/**
 *  Player
 *
 *  The [Player] Data Class is the representation of a Game in the system.
 *
 *  @param pid The player identifier
 *  @param name The player name
 *  @param email The player email
 */
@Serializable
data class Player (
        val pid: Int,
        val name: String,
        val email: String
) {
        init {
                require(pid >= 0) { "The player identifier must be a positive integer" }
                require(name.isNotEmpty()) { "The player name must not be empty" }
                require(email.isNotEmpty()) { "The player email must not be empty" }
        }
}