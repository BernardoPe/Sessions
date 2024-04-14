package pt.isel.ls.storage

import pt.isel.ls.storage.db.DBConnectionManager
import java.io.Closeable

class SessionsDataManager(
    val game: SessionsDataGame,
    val player: SessionsDataPlayer,
    val session: SessionsDataSession,
) : Closeable {

    private val connectionManager = DBConnectionManager()
    override fun close() {
        connectionManager.closeAll()
    }

}
