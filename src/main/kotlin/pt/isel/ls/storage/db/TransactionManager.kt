package pt.isel.ls.storage.db

import org.postgresql.ds.PGSimpleDataSource
import org.postgresql.util.PSQLException
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.InternalServerErrorException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.logger
import java.sql.Connection

/**
 * Handles DB connections and transactions for multiple threads
 *
 *
 * This class internally uses a [MutableMap] to store the connections for each thread
 * When a thread requests a connection, it will either return the existing connection or create a new one.
 *
 * The [setIsolationLevel] method is used to set the isolation level for the current connection
 *
 * The [begin] method is used to start a transaction for the current thread.
 *
 * The [abort] method is used to abort a transaction for the current thread.
 *
 * The [finish] method is used to finish a transaction for the current thread.
 *
 * The [handlePSQLException] method is used to handle a [PSQLException] and return the appropriate exception.
 *
 * The [closeAll] method is used to close all connections currently stored.
 *
 * The [closeThreadConnection] method is used to close the connection for the current thread.
 *
 * @property getConnection gets the connection for the current thread

 */
object TransactionManager {


        /**
         * Gets a new connection to the database
         *
         * It uses the [PGSimpleDataSource] to create a new connection to the database
         *
         * @return The new connection
         */
        private fun getNewConnection(dbUrl: String): Connection {
            logger.info("Creating new connection to database")
            val newSource = PGSimpleDataSource()
            newSource.setUrl(dbUrl)
            return newSource.connection
        }

        /**
         * Gets the connection for the current thread
         *
         * If the connection is closed, it will create a new connection
         *
         * @return The connection for the current thread
         */
        fun getConnection(dbUrl: String): Connection {
            logger.info("Getting connection to database")
            val connection = connections.getOrPut(Thread.currentThread().id) { getNewConnection(dbUrl) }
            if (connection.isClosed) {
                connections[Thread.currentThread().id] = getNewConnection(dbUrl)
            }
            return connections[Thread.currentThread().id]!!
        }

    /**
     * Begins a transaction for the current thread
     * @param dbUrl The database URL
     */
        fun begin(dbUrl: String) {
            val connection = connections.getOrPut(Thread.currentThread().id) { getNewConnection(dbUrl) }
            connection.autoCommit = false
            connection.beginRequest()
            logger.info("Transaction started")
        }

        fun setIsolationLevel(dbUrl: String, isolationLevel: Int) {
            val connection = getConnection(dbUrl)
            connection.transactionIsolation = isolationLevel
            logger.info("Isolation level set to $isolationLevel for current transaction")
        }

        /**
         * Aborts a transaction for the current thread
         *
         */

        fun abort(dbUrl: String) {
            val connection = getConnection(dbUrl)
            connection.rollback()
            connection.endRequest()
            connection.autoCommit = true
            logger.info("Transaction aborted")
        }

        /**
         * Finishes a transaction for the current thread
         */
        fun finish(dbUrl: String) {
            val connection = getConnection(dbUrl)
            connection.commit()
            connection.endRequest()
            connection.autoCommit = true
            logger.info("Transaction finished")
        }

        // Map that stores the connections for each thread
        private val connections = mutableMapOf<Long, Connection>()

        /**
         * Closes the connection for the thread that calls this method
         *
         * If the connection is not closed, it will roll back any transaction that is in progress and then close the connection
         *
         * If the connection is closed, the function will have no effect
         */
        fun closeThreadConnection() {
            connections.remove(Thread.currentThread().id)?.let {
                if (!it.isClosed) {
                    if (!it.autoCommit) //mid transaction
                        it.rollback()
                    it.close()
                }
            }
            logger.info("Connection closed")
        }

        /**
         * Closes all connections currently stored
         *
         * If a connection is not closed, it will roll back any transaction that is in progress and then close the connection
         * If a connection is closed, the function will have no effect
         *
         * This method is useful when the application is shutting down
         */
        fun closeAll() {
            logger.info("Closing all connections...")
            connections.values.forEach {
                if (!it.isClosed) {
                    if (!it.autoCommit) // mid transaction
                        it.rollback()
                    it.close()
                    logger.info("Connection closed")
                }
            }
        }


        fun handlePSQLException(e: Exception) : Exception {
            when (e) {
                is PSQLException -> {
                    val message = e.message?.lowercase()
                    return when (e.sqlState) {
                        "23505" -> {
                            // unique constraint violation
                            when {
                                message?.contains("players_name_key") == true -> BadRequestException("Player name already exists")
                                message?.contains("players_email_key") == true -> BadRequestException("Player email already exists")
                                message?.contains("games_name_key") == true -> BadRequestException("Game name already exists")
                                message?.contains("sessions_players_pkey") == true -> BadRequestException("Player already in session")
                                else -> InternalServerErrorException()
                            }
                        }
                        "23503" -> {
                            // foreign key violation
                            when {
                                message?.contains("fk_session_game") == true -> NotFoundException("Game not found")
                                message?.contains("fk_session_player") == true -> NotFoundException("Player not found")
                                message?.contains("sessions_game_id_fkey") == true -> NotFoundException("Game not found")
                                message?.contains("sessions_players_player_id_fkey") == true -> NotFoundException("Player not found")
                                message?.contains("sessions_players_session_id_fkey") == true -> NotFoundException("Session not found")
                                else -> InternalServerErrorException()
                            }
                        }
                        else -> InternalServerErrorException()
                    }
                }
                else -> return InternalServerErrorException()
            }
        }

}