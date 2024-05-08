package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
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
    val pid get() = players.size.toUInt() + 1u
    val sid get() = sessions.size.toUInt() + 1u
    val gid get() = games.size.toUInt() + 1u

    companion object {
        private val players = mutableListOf(
            Player(1u, Name("John Doe"), Email("testemail@a.pt"), 0L), // for tests
        )
        private val sessions = mutableListOf<Session>()
        private val games = mutableListOf<Game>()
    }

    abstract fun clear() // each subclass should clear its own data

}