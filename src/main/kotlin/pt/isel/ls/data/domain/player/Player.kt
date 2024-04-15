package pt.isel.ls.data.domain.player

import pt.isel.ls.data.domain.util.Email
import pt.isel.ls.data.domain.util.Name

/**
 *  Player
 *
 *  The [Player] Data Class is the representation of a Game in the system.
 *
 *  @param id The player identifier
 *  @param name The player name
 *  @param email The player email
 *  @param token The player token
 */
data class Player(
    val id: UInt,
    val name: Name,
    val email: Email,
    val token: Long,
)
