package pt.isel.ls.domain.player

import pt.isel.ls.domain.DomainMapper
import pt.isel.ls.dto.PlayerInfoOutputModel

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
) : DomainMapper<PlayerInfoOutputModel> {
        init {
                require(pid >= 0) { "The player identifier must be a positive integer" }
                require(name.isNotBlank()) { "The player name must not be empty" }
                require(email.isNotBlank()) { "The player email must not be empty" }
                require(name.length in 1..40) { "The player name must be between 1 and 40 characters" }
                require(email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$"))) { "The player email must be a valid e-mail" }
        }

        override fun toDTO() = PlayerInfoOutputModel(pid, name, email)

}

