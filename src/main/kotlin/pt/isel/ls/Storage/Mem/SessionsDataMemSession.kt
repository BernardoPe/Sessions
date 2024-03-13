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
     * @param value The session object to be created
     */
    override fun create(value: Session) {
        super.create(value)
    }

    /**
     * Read a session from the database mock
     *
     * This function uses the [read] function from the [SessionsDataMem] class
     *
     * @param id The session identifier
     * @return The session object with the given id or null if it does not exist
     */
    override fun read(id: Int): Session? {
        return super.read(id)
    }

    /**
     * Update a session in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMem] class
     *
     * @param id The session identifier
     * @param value The new session object
     */
    override fun update(id: Int, value: Session) {
        super.update(id, value)
    }

    /**
     * Delete a session from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMem] class
     *
     * @param id The session identifier
     */
    override fun delete(id: Int) {
        super.delete(id)
    }
}