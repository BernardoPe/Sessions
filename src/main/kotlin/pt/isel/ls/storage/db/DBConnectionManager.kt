package pt.isel.ls.storage.db

import org.postgresql.ds.PGSimpleDataSource
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
 * @property connection the connection to the database for the current thread

 */
class DBConnectionManager {
    private fun getNewConnection(): Connection {
        val newSource = PGSimpleDataSource()
        newSource.setUrl(System.getenv("JDBC_DATABASE_URL"))
        return newSource.connection
    }

    /**
     * Current thread DB connection
     */
    fun getConnection(): Connection {
        val connection = connections.getOrPut(Thread.currentThread().id) { getNewConnection() }
        if (connection.isClosed) {
            connections[Thread.currentThread().id] = getNewConnection()
        }
        return connections[Thread.currentThread().id]!!
    }
    companion object {
        private val connections = mutableMapOf<Long, Connection>()
    }

    /**
     * Closes the connection for the thread that calls this method
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