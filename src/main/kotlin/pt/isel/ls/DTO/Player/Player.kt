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
)