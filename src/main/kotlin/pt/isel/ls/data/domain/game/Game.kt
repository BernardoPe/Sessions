package pt.isel.ls.data.domain.game

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
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
        val id: UInt,
        val name: Name,
        val developer: Name,
        val genres: Set<Genre>
)