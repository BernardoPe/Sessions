package pt.isel.ls.storage

import org.postgresql.util.PSQLException
import pt.isel.ls.storage.db.TransactionManager
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
    private val type: DataManagerType,
    private val dbURL: String? = null,
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
     * Executes a transaction.
     * @param query transaction to execute.
     * @return the result of the transaction.
     *
     * If the transaction fails, it aborts the transaction and throws an exception.
     */

    fun <T> executeTransaction(query: (SessionsDataManager) -> T) : T {
        beginTransaction()
        val ret : T
        try {
            ret = query(this)
        } catch (e: Exception) {
            abortTransaction()
            if (e is PSQLException) {
                throw TransactionManager.handlePSQLException(e)
            } else {
                throw e
            }
        }
        finishTransaction()
        return ret
    }

    private fun beginTransaction() {
        if (type == DataManagerType.DATABASE) {
            TransactionManager.begin(dbURL!!)
        }
    }

    private fun finishTransaction() {
        if (type == DataManagerType.DATABASE) {
            TransactionManager.finish(dbURL!!)
        }
    }

    private fun abortTransaction() {
        if (type == DataManagerType.DATABASE) {
            TransactionManager.abort(dbURL!!)
        }
    }

    /**
     * Closes all the data managers.
     */
    override fun close() {
        if (type == DataManagerType.DATABASE) {
            TransactionManager.closeAll()
        } else {
            (game as SessionsDataMemGame).clear()
            (player as SessionsDataMemPlayer).clear()
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
