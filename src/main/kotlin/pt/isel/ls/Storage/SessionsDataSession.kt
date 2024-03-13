package pt.isel.ls.Storage

import pt.isel.ls.DTO.Session.Session

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
        fun create(value: Session)

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
        fun getAll(): List<Session>

        /**
        * Update a session in the database
        *
        * @param id The session identifier
        * @param value The new session object
        */
        fun update(id: Int, value: Session)

        /**
        * Delete a session from the database
        *
        * @param id The session identifier
        */
        fun delete(id: Int)
}