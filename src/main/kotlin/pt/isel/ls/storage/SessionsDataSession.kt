package pt.isel.ls.storage

import pt.isel.ls.domain.game.Game
import pt.isel.ls.domain.session.Session

/**
 *  Game Data management interface
 *
 *  This interface is used to manage the game data
 *
 *  @property create Create a session in the database
 *  @property getById Read a session from the database
 *  @property getAll Read all sessions from the database
 *  @property update Update a session in the database
 *  @property delete Delete a session from the database
 */
interface SessionsDataSession {

        /**
        * Create a session in the database
        *
        * @param value The session object to be created
        */
        fun create(capacity: Int, game: Game, date: String): Int

        /**
        * Read a session from the database
        *
        * @param id The session identifier
        * @return The session object with the given id or null if it does not exist
        */
        fun getById(id: Int): Session?

        /**
         * Read all sessions from the database
         *
         * @return A list with all the sessions in the database
         */
        fun getSessionsSearch(gid: Int, date: String?, state: String?, pid: Int?, limit: Int, skip: Int): List<Session>

        /**
        * Update a session in the database
        *
        * @param sid The session identifier
        * @param pid The player identifier
        */
        fun update(sid: Int, pid: Int)

        /**
        * Delete a session from the database
        *
        * @param id The session identifier
        * @return true if the session was deleted, false otherwise
        */
        fun delete(id: Int)
}