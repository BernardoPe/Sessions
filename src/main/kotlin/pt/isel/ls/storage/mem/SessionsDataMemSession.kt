package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.storage.SessionsDataSession

/**
 *  SessionsDataMemSession
 *
 *  Session Data management class
 *
 *  Uses the [SessionsDataMemSession] class to manage the session data
 *
 */

class SessionsDataMemSession : SessionsDataSession {

    /**
     * Database Mock
     *
     * This is a mockup of the database, used for testing purposes.
     *
     * @property db The database.
     */
    private var db: MutableList<Session> = mutableListOf()

    /**
     * Last Identifier
     *
     * The last identifier is used to keep track of the last identifier used in the database mock
     * When a new session instance is added to the database mock, the last identifier is incremented
     *
     * @property lastId The last identifier.
     */
    private var lastId = 0

    /**
     * Create a session in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMemSession] class
     *
     * @param value The session object to be created
     */
    override fun create(value: Session) {
        // Add the session object to the database mock
        // Start by incrementing the last identifier
        lastId++
        // Add the updated session object to the database mock
        db.add(
            Session(
                lastId,
                value.capacity,
                value.date,
                value.gameSession,
                value.playersSession
            )
        )
    }

    /**
     * Read a session from the database mock
     *
     * This function uses the [get] function from the [SessionsDataMemSession] class
     *
     * @param id The session identifier
     * @return The session object with the given id or null if it does not exist
     */
    override fun getById(id: Int): Session? {
        // Read the session object from the database mock
        db.forEach {
            // search for the session with the given id
            if (it.sid == id) {
                // if found
                // return the session object
                return it
            }
        }
        return null
    }

    /**
     * Read all sessions from the database mock
     *
     * This function uses the [getAll] function from the [SessionsDataSessionMem] class
     *
     * @return A list with all the sessions in the database
     */
    override fun getAll(): List<Session> {
        // Read all the session objects from the database mock
        return db
    }

    /**
     * Update a session in the database mock
     *
     * This function uses the [update] function from the [SessionsDataSessionMem] class
     *
     * @param id The session identifier
     * @param value The new session object
     */
    override fun update(id: Int, value: Session): Boolean {
        // Update the session object in the database mock
        db.forEach {
            // search for the session with the given id
            if (it.sid == id) {
                // if found
                // remove the session from the database mock
                db.remove(it)
                // add the new session to the database mock
                db.add(value)
                // tell the caller that the update was successful
                return true
            }
        }
        // tell the caller that the update was not successful
        return false
    }

    /**
     * Delete a session from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataSessionMem] class
     *
     * @param id The session identifier
     */
    override fun delete(id: Int): Boolean {
        // Delete the session object from the database mock
        db.forEach {
            // search for the session with the given id
            if (it.sid == id) {
                // if found
                // remove the session from the database mock
                db.remove(it)
                // tell the caller that the delete was successful
                return true
            }
        }
        // tell the caller that the delete was not successful
        return false
    }
}