package pt.isel.ls.storage


import org.postgresql.util.PSQLException
import pt.isel.ls.exceptions.InternalServerErrorException
import pt.isel.ls.logger
import pt.isel.ls.storage.db.TransactionManager
import pt.isel.ls.storage.db.SessionsDataDBGame
import pt.isel.ls.storage.db.SessionsDataDBPlayer
import pt.isel.ls.storage.db.SessionsDataDBSession
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import java.io.Closeable


/**
 * Abstract class that manages the data for all the different entities.
 * Implemented by [DBManager] and [MemManager].
 *
 * @property game The game data manager.
 * @property player The player data manager.
 * @property session The session data manager.
 *
 * @property executeTransaction Executes a transaction, and returns the result.
 *
 * @property close Closes all the data managers.
 */
abstract class SessionsDataManager : Closeable {

    /** The game data manager. */
    abstract val game : SessionsDataGame
    /** The player data manager. */
    abstract val player : SessionsDataPlayer
    /** The session data manager. */
    abstract val session : SessionsDataSession

    /**
     * Executes a transaction. If the transaction fails, it aborts the transaction and throws an exception.
     */
    abstract fun <T> executeTransaction(query: (SessionsDataManager) -> T) : T

    /** Executes a transaction with a specific isolation level.
     *  If the storage is memory based, the isolation level is ignored.
     *  If it is a database, the isolation level is used only for this transaction.
     *
     */
    abstract fun <T> executeTransaction(query: (SessionsDataManager) -> T, isolationLevel: Int) : T

    /**
     * Closes all the data managers.
     */
    abstract override fun close()
}


/**
 * Database storage manager class.
 *
 * Implements the [SessionsDataManager] abstract class.
 *
 * @param dbURL The database URL.
 *
 * @property game The game data manager.
 * @property player The player data manager.
 * @property session The session data manager.
 *
 * @property executeTransaction Executes a transaction, and returns the result.
 *
 */
class DBManager(
    private val dbURL: String
) : SessionsDataManager() {

    override val game : SessionsDataGame = SessionsDataDBGame(dbURL)
    override val player : SessionsDataPlayer = SessionsDataDBPlayer(dbURL)
    override val session : SessionsDataSession = SessionsDataDBSession(dbURL)

    private val maxRetries = 5

    override fun <T> executeTransaction(query: (SessionsDataManager) -> T) : T {
        var retries = 0
        while (retries < maxRetries) {
            TransactionManager.begin(dbURL)
            try {
                val ret = query(this)
                TransactionManager.finish(dbURL)
                return ret
            } catch (e: Exception) {
                TransactionManager.abort(dbURL)
                if (e is PSQLException) {
                    if (e.sqlState == "40001") {
                        logger.error("Transaction failed due to serialization failure. Retrying...")
                        // serialization failure
                        retries++
                    } else {
                        throw TransactionManager.handlePSQLException(e)
                    }
                } else {
                    throw e
                }
            }
        }
        logger.error("Transaction failed after $maxRetries retries.")
        throw InternalServerErrorException()
    }

    override fun <T> executeTransaction(query: (SessionsDataManager) -> T, isolationLevel: Int) : T {
        val prevIsolationLevel = TransactionManager.getConnection(dbURL).transactionIsolation
        TransactionManager.setIsolationLevel(dbURL, isolationLevel)
        val ret = executeTransaction(query)
        TransactionManager.setIsolationLevel(dbURL, prevIsolationLevel)
        return ret
    }

    override fun close() {
        TransactionManager.closeAll()
    }
}

/**
 * Memory storage manager class.
 *
 * Implements the [SessionsDataManager] abstract class.
 *
 * @property game The game data manager.
 * @property player The player data manager.
 * @property session The session data manager.
 *
 * @property executeTransaction Executes a transaction, and returns the result.
 *
 */
class MemManager : SessionsDataManager() {

    override val game : SessionsDataGame = SessionsDataMemGame()
    override val player : SessionsDataPlayer = SessionsDataMemPlayer()
    override val session : SessionsDataSession = SessionsDataMemSession()

    override fun <T> executeTransaction(query: (SessionsDataManager) -> T) : T {
        return try {
            query(this)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun <T> executeTransaction(query: (SessionsDataManager) -> T, isolationLevel: Int) : T {
        return executeTransaction(query)
    }

    override fun close() {
        (game as SessionsDataMemGame).clear()
        (player as SessionsDataMemPlayer).clear()
        (session as SessionsDataMemSession).clear()
    }

}