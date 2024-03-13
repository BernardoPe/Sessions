package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Session.Session
import pt.isel.ls.Storage.SessionsDataSession

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
    var db: MutableList<Session> = mutableListOf()

    /**
     * Create a session in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMemSession] class
     *
     * @param value The session object to be created
     */
    override fun create(value: Session) {
        // Add the session object to the database mock
        db.add(value)
    }

    /**
     * Read a session from the database mock
     *
     * This function uses the [get] function from the [SessionsDataSessionMem] class
     *
     * @param id The session identifier
     * @return The session object with the given id or null if it does not exist
     */
    override fun getById(id: Int): Session? {
        // Read the session object from the database mock
        db.forEach {
            // search for the session with the given id
            if (it.ssid == id) {
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
    override fun update(id: Int, value: Session) {
        // Update the session object in the database mock
        db.forEach {
            // search for the session with the given id
            if (it.gid == id) {
                // if found
                // remove the session from the database mock
                db.remove(it)
                // add the new session to the database mock
                db.add(value)
            }
        }
    }

    /**
     * Delete a session from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataSessionMem] class
     *
     * @param id The session identifier
     */
    override fun delete(id: Int) {
        // Delete the session object from the database mock
        db.forEach {
            // search for the session with the given id
            if (it.gid == id) {
                // if found
                // remove the session from the database mock
                db.remove(it)
            }
        }
    }
}