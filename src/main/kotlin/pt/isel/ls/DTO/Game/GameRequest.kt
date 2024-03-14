package pt.isel.ls.DTO.Game

import kotlinx.serialization.Serializable

/**
 * The [GameRequest] class is used to represent the request body of a game
 *
 * If an empty developer, name or genre is provided, an exception is thrown
 *
 * @param name The game name
 * @param developer The game developer
 * @param genres The list of genres
 *
 *
 */
@Serializable
data class GameRequest (
    val name: String,
    val developer: String,
    val genres: List<String>
) {
    init {
        require(name.isNotEmpty()) { "The game name must not be empty" }
        require(developer.isNotEmpty()) { "The game developer must not be empty" }
        require(
            genres.all { it.isNotEmpty() }
        ) { "The game genres must not be empty" }
    }
}

