package pt.isel.ls.storage

import pt.isel.ls.storage.db.SessionsDataDBGame
import pt.isel.ls.storage.db.SessionsDataDBPlayer
import pt.isel.ls.storage.db.SessionsDataDBSession
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import java.io.Closeable

/**
 * Manages data for all the different entities.
 *
 * @param DataManagerType The type of data manager to use.
 * @param dbURL The URL of the database. This parameter is only required if the data manager type is DATABASE.
 *
 * @property close Closes all the data managers. This function has no effect if the data managers are not database data managers.
 * If they are database data managers, it closes all of their connections.
 *
 */
class SessionsDataManager(
    type: DataManagerType,
    dbURL: String? = null,
) : Closeable {

    val game : SessionsDataGame
    val player : SessionsDataPlayer
    val session : SessionsDataSession
    init {
        if (type == DataManagerType.DATABASE) {
            require(dbURL != null) {"No Database URL provided."}
            game = SessionsDataDBGame(dbURL)
            player = SessionsDataDBPlayer(dbURL)
            session = SessionsDataDBSession(dbURL)
        } else {
            game = SessionsDataMemGame()
            player = SessionsDataMemPlayer()
            session = SessionsDataMemSession()
        }
    }

    /**
     * Closes all the data managers.
     */
    override fun close() {
        if (game is SessionsDataDBGame) {
            game.closeAll()
        } else {
            (game as SessionsDataMemGame).clear()
        }
        if (player is SessionsDataDBPlayer) {
            player.closeAll()
        } else {
            (player as SessionsDataMemPlayer).clear()
        }
        if (session is SessionsDataDBSession) {
            session.closeAll()
        } else {
            (session as SessionsDataMemSession).clear()
        }
    }
}

/**
 * The different types of data managers.

 */
enum class DataManagerType {
    MEMORY, DATABASE
}
