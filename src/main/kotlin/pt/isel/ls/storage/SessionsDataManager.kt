package pt.isel.ls.storage

import pt.isel.ls.storage.db.SessionsDataDBGame
import pt.isel.ls.storage.db.SessionsDataDBPlayer
import pt.isel.ls.storage.db.SessionsDataDBSession
import java.io.Closeable

/**
 * Manages data for all the different entities.
 *
 * @param game The game data manager.
 * @param player The player data manager.
 * @param session The session data manager.
 *
 * @property close Closes all the data managers. This function has no effect if the data managers are not database data managers.
 * If they are database data managers, it closes all of their connections.
 *
 */
class SessionsDataManager(
    val game: SessionsDataGame,
    val player: SessionsDataPlayer,
    val session: SessionsDataSession,
) : Closeable {

    /**
     * Closes all the data managers.
     * This function has no effect on the data managers if they are not DB data managers.
     */
    override fun close() {
        if (game is SessionsDataDBGame) {
            game.closeAll()
        }
        if (player is SessionsDataDBPlayer) {
            player.closeAll()
        }
        if (session is SessionsDataDBSession) {
            session.closeAll()
        }
    }

}
