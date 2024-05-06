package pt.isel.ls.storage.db

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.exceptions.InternalServerErrorException
import pt.isel.ls.logger
import java.sql.Connection

/**
 * Handles DB connections for multiple threads
 *
 *
 * This class internally uses a [MutableMap] to store the connections for each thread
 * When a thread requests a connection, it will either return the existing connection or create a new one.
 *
 * The [closeAll] method is used to close all connections currently stored.
 *
 * The [closeThreadConnection] method is used to close the connection for the current thread.
 *
 * @property getConnection gets the connection for the current thread

 */
open class DBManager(
    private val dbUrl : String
) {
    /**
     * Executes a query on the database
     * @param query The query to execute
     * @return The result of the query
     * @throws InternalServerErrorException If an error occurs while executing the query. Since data integrity restrictions and
     * business logic are expected to be validated before calling this method, this exception is thrown when an unexpected error occurs.
     */
    fun execQuery(query: (Connection) -> Any?) : Any? {
        val connection = getConnection()
        connection.autoCommit = false
        val ret : Any?
        try {
            ret = query(connection)
            connection.commit()
        } catch (e: Exception) {
            logger.error("Error while executing query", e)
            connection.rollback()
            // an error occurred that is not related to the request validation
            throw InternalServerErrorException()
        }
        finally {
            connection.autoCommit = true
        }
        return ret
    }


    /**
     * Gets a new connection to the database
     *
     * It uses the [PGSimpleDataSource] to create a new connection to the database
     *
     * @return The new connection
     */
    private fun getNewConnection(): Connection {
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
    private fun getConnection(): Connection {
        val connection = connections.getOrPut(Thread.currentThread().id) { getNewConnection() }
        if (connection.isClosed) {
            connections[Thread.currentThread().id] = getNewConnection()
        }
        return connections[Thread.currentThread().id]!!
    }

    companion object {
        // Map that stores the connections for each thread
        private val connections = mutableMapOf<Long, Connection>()
    }

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
        connections.values.forEach {
            if (!it.isClosed) {
                if (!it.autoCommit) //mid transaction
                    it.rollback()
                it.close()
            }
        }
    }
}