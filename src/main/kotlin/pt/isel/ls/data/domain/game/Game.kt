package pt.isel.ls.data.domain.game

import pt.isel.ls.dto.GameInfoOutputModel

/**
 *  Game
 *
 *  The [Game] Data Class is the representation of a Game in the system.
 *
 *  @param gid The game identifier
 *  @param developer The game developer
 *  @param genres The game list of genres
 */
data class Game (
        val gid: Int,
        val name: String,
        val developer: String,
        val genres: Set<String>
) {
        init {
                require(gid >= 0) { "The game identifier must be a positive integer" }
                require(name.isNotBlank() ) { "The game name must not be empty" }
                require(developer.isNotBlank()) { "The game developer must not be empty" }
                require(
                        genres.all { it.isNotBlank() }
                ) { "The game genres must not be empty" }
                require(name.length in 1..40) { "The game name must be between 1 and 40 characters" }
                require(developer.length in 1..40) { "The game developer must be between 1 and 40 characters" }
                require(genres.all { it.length in 1..40 }) { "The game genres must be between 1 and 40 characters" }
        }

}


