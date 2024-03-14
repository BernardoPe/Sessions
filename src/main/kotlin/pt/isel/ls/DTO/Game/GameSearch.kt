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
    val genres: List<String>
)
