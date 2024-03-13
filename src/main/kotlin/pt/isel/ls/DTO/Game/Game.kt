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
) {
        init {
                require(gid >= 0) { "The game identifier must be a positive integer" }
                require(name.isNotEmpty()) { "The game name must not be empty" }
                require(developer.isNotEmpty()) { "The game developer must not be empty" }
                require(genres.isNotEmpty()) { "The game genres must not be empty" }
                require(
                        genres.all { it.isNotEmpty() }
                ) { "The game genres must not be empty" }
        }
}


