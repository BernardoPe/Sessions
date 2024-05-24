package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.player.Token
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.data.domain.session.Session

/**
 * MemManager
 *
 * Abstract class to manage the in-memory database
 *
 * This storage is persistent only during the application runtime

 */
abstract class MemManager {

    val playerDB get() = players
    val sessionDB get() = sessions
    val gameDB get() = games
    val tokenDB get() = tokens
    val pid get() = players.size.toUInt() + 1u
    val sid get() = sessions.size.toUInt() + 1u
    val gid get() = games.size.toUInt() + 1u

    companion object {
        private val players = mutableListOf(
            Player(
                1u,
                Name("John Doe"),
                Email("testemail@a.pt"),
                PasswordHash("\$2a\$10\$e0NRHJk/WZz4o6sW0IKZxeQJX5X/0y5Q7HRUBqKEXzSo1QzDOOXSi") // for tests
            )
        )
        private val sessions = mutableListOf<Session>()
        private val games = mutableListOf<Game>()
        private val tokens = mutableListOf<Token>()
    }

    abstract fun clear() // each subclass should clear its own data

}