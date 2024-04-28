package pt.isel.ls.data.domain.player

import pt.isel.ls.data.domain.util.Email
import pt.isel.ls.data.domain.util.Name

/**
 *  Player
 *
 *  The [Player] Data Class is the representation of a Game in the system.
 *
 * @param id The player's id (unique [UInt] number)
 * @param name The player's name (unique [Name] object)
 * @param email The player's email (unique [Email] object)
 * @param token The player's token (unique [Long] number)
 */
data class Player(
    val id: UInt,
    val name: Name,
    val email: Email,
    val token: Long,
)
