package pt.isel.ls.storage

import pt.isel.ls.storage.db.DBManager
import pt.isel.ls.storage.db.SessionsDataDBGame
import pt.isel.ls.storage.db.SessionsDataDBPlayer
import pt.isel.ls.storage.db.SessionsDataDBSession
import java.io.Closeable

class SessionsDataManager(
    val game: SessionsDataGame = SessionsDataDBGame(),
    val player: SessionsDataPlayer = SessionsDataDBPlayer(),
    val session: SessionsDataSession = SessionsDataDBSession(),
) : Closeable {

    private val connectionManager = DBManager()
    override fun close() {
        connectionManager.closeAll()
    }

}
