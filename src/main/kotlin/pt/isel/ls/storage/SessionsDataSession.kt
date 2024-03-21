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
 *  @property update Update a session in the database
 *  @property delete Delete a session from the database
 */
interface SessionsDataSession {

        /**
         * Create a session in the database mock
         *
         * This function creates a session object and adds it to the database mock
         * The function does not have a Session as parameter, but the fields that are needed to create a session
         * The playerSession is an empty set by default
         *
         * @param session The [Session] object
         * @return The session identifier
         */
        fun create(session: Session): UInt

        /**
         * Read a session from the database mock
         *
         * This function gets the session object from the database mock with the given id
         *
         * @param id The session identifier
         * @return The session object with the given id or null if it does not exist
         */
        fun getById(id: UInt): Session?

        /**
         *  Read sessions from the database mock that match the given parameters
         *
         *  The [getSessionsSearch] function searches the database mock for sessions that match the given parameters
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
         * Update a session in the database mock
         *
         * This function updates the session object in the database mock with the given id
         * The function does not have a Session as parameter, but the fields that are needed to update a session
         *
         * @param sid The session identifier
         * @param player The player object
         */
        fun update(sid: UInt, player: Player) : Boolean

        /**
         * Delete a session from the database mock
         *
         * This function deletes the session object from the database mock with the given id
         *
         * @param id The session identifier
         */
        fun delete(id: UInt) : Boolean
}