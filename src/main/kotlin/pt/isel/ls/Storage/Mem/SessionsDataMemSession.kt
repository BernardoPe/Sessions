package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Session.Session


// Getter function
private val getter: (Session, Int) -> Boolean = { session, id -> session.ssid == id }

/**
 *  SessionsDataMemSession
 *
 *  Session Data management class
 *
 *  Uses the [SessionsDataMem] class to manage the session data
 *
 *  In this case the [getter] is a lambda function used as a comparator
 *
 */

class SessionsDataMemSession : SessionsDataMem<Session>(getter) {

    /**
     * Create a session in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMem] class
     *
     * @param session The session object to be created
     */
    fun createSession(session: Session) {
        // Add the session object to the database mock
        create(session)
    }

    /**
     * Read a session from the database mock
     *
     * This function uses the [read] function from the [SessionsDataMem] class
     *
     * @param id The session identifier
     * @return The session object with the given id or null if it does not exist
     */
    fun readSession(id: Int): Session? {
        return read(id)
    }

    /**
     * Update a session in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMem] class
     *
     * @param id The session identifier
     * @param session The new session object
     */
    fun updateSession(id: Int, session: Session) {
        // Update the session in the database mock with the new session object
        update(id, session)
    }

    /**
     * Delete a session from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMem] class
     *
     * @param id The session identifier
     */
    fun deleteSession(id: Int) {
        // Delete the session from the database mock with the given id
        delete(id)
    }
}