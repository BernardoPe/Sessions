package pt.isel.ls.Control.DTO.Game

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
        val developer: String,
        val genres: List<String>,
)


