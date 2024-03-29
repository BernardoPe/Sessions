package pt.isel.ls.storage

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State

/**
 *  Game Data management interface
 *
 *  This interface is used to manage the game data
 *
 *  @property create Create a session in the database
 *  @property getById Read a session from the database
 *  @property addPlayer Update a session in the database
 *  @property removePlayer Remove a player from a session in the database
 *  @property update Update the capacity and date of a session in the database
 *  @property delete Delete a session from the database
 */
interface SessionsDataSession {

        /**
         * Create a session in the database 
         *
         * This function creates a session object and adds it to the database
         *
         * @param session The [Session] object
         * @return The session identifier
         */
        fun create(session: Session): UInt

        /**
         * Read a session from the database 
         *
         * This function gets the session object from the database with the given id
         *
         * @param id The session identifier
         * @return The session object with the given id or null if it does not exist
         */
        fun getById(id: UInt): Session?

        /**
         *  Read sessions from the database that match the given parameters
         *
         *  The [getSessionsSearch] function searches the database for sessions that match the given parameters
         *
         *  @param gid The game identifier
         *  @param date The date
         *  @param state The state
         *  @param pid The player identifier
         *  @param limit The maximum number of sessions to be returned
         *  @param skip The number of sessions to be skipped
         *  @return A list with all the sessions in the database that match the given parameters
         */
        fun getSessionsSearch(gid: UInt, date: LocalDateTime?, state: State?, pid: UInt?, limit: UInt, skip: UInt): List<Session>

        /**
         * Add a player to a session in the database 
         *
         * This function adds a player object to the session object in the database with the given id
         *
         * @param sid The session identifier
         * @param player The player object
         */
        fun addPlayer(sid: UInt, player: Player) : Boolean

        /**
         * Remove a player from a session in the database 
         *
         * This function removes a player object from the session object in the database with the given id
         *
         * @param sid The session identifier
         * @param pid The player identifier
         * @return boolean indicating if the removal was successful
         */
        fun removePlayer(sid: UInt, pid: UInt) : Boolean

        /**
         * Update a session in the database 
         *
         * This function updates the session object in the database with the given id
         *
         * @param sid The session identifier
         * @param capacity The new capacity
         * @param date The new date
         * @return boolean indicating if the update was successful
         */
        fun update(sid: UInt, capacity: UInt, date: LocalDateTime) : Boolean

        /**
         * Delete a session from the database 
         *
         * This function deletes the session object from the database with the given id
         *
         * @param id The session identifier
         */
        fun delete(id: UInt) : Boolean
}