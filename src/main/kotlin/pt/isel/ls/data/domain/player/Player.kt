package pt.isel.ls.data.domain.player

import pt.isel.ls.data.domain.Email
import pt.isel.ls.data.domain.Name
import java.util.UUID


/**
 *  Player
 *
 *  The [Player] Data Class is the representation of a Game in the system.
 *
 *  @param pid The player identifier
 *  @param name The player name
 *  @param email The player email
 */
data class Player (
        val id: UInt,
        val name: Name,
        val email: Email,
        val token : UUID
)
