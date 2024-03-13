package pt.isel.ls.DTO.Player

import kotlinx.serialization.SerialName
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
        @SerialName("pid") val pid: Int,
        @SerialName("name") val name: String,
        @SerialName("email") val email: String,
        @SerialName("token") val token: String
) {
        init {
                require(pid >= 0) { "The player identifier must be a positive integer" }
                require(name.isNotEmpty()) { "The player name must not be empty" }
                require(email.isNotEmpty()) { "The player email must not be empty" }
                require(token.isNotEmpty()) { "The player token must not be empty" }
        }
}