package pt.isel.ls.DTO.Player

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
        val pid: Int,
        val name: String,
        val email: String,
)