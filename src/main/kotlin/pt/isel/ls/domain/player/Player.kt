package pt.isel.ls.domain.player

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
        val email: String
) {
        init {
                require(pid >= 0) { "The player identifier must be a positive integer" }
                require(name.isNotBlank()) { "The player name must not be empty" }
                require(email.isNotBlank()) { "The player email must not be empty" }
        }
}
