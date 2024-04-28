package pt.isel.ls.data.domain.game

import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.domain.util.Name

/**
 *  Game
 *
 *  The [Game] Data Class is the representation of a Game in the system.
 *
 *  @param id The game identifier (unique [UInt] number)
 *  @param name The game name ([Name] object)
 *  @param developer The game developer ([Name] object)
 *  @param genres The game genres ([Set] of [Genre] objects)
 *
 *  @throws IllegalArgumentException If the game has no genres
 */
data class Game(
    val id: UInt,
    val name: Name,
    val developer: Name,
    val genres: Set<Genre>,
) {
    init {
        require(genres.isNotEmpty()) { "Game must have at least one genre" }
    }
}
