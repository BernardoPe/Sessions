package pt.isel.ls.DTO.Game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *  Game
 *
 *  The [Game] Data Class is the representation of a Game in the system.
 *
 *  @param gid The game identifier
 *  @param developer The game developer
 *  @param genres The game list of genres
 */
@Serializable
data class Game (
        @SerialName("gid") val gid: Int,
        @SerialName("name") val name: String,
        @SerialName("developer") val developer: String,
        @SerialName("genres") val genres: List<String>
)


