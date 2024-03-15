package pt.isel.ls.DTO.Game

import kotlinx.serialization.Serializable

/**
 * The [GameSearch] class is used to represent the request body of a game search
 * @param developer The game developer
 * @param genres The list of genres
 */
@Serializable
data class GameSearch (
    val developer: String,
    val genres: Set<String>
) {
    init {
        require(developer.isNotBlank()) { "The game developer must not be empty" }
        require(
            genres.all { it.isNotBlank() }
        ) { "The game genres must not be empty" }
        require(developer.length in 1..40) { "The game developer must be between 1 and 40 characters" }
        require(genres.all { it.length in 1..40 }) { "The game genres must be between 1 and 40 characters" }
    }
}
