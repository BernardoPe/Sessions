package pt.isel.ls.storage.mem

import pt.isel.ls.domain.game.Game
import pt.isel.ls.domain.session.Session
import pt.isel.ls.exceptions.SessionNotFoundException
import pt.isel.ls.storage.SessionsDataSession

/**
 *  SessionsDataMemSession
 *
 *  Session Data management class
 */

class SessionsDataMemSession : SessionsDataSession {

    /**
     * Database Mock
     *
     * This is a mockup of the database, used for testing purposes.
     * The database mock is a mutable list of session objects
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

    override fun create(capacity: Int, game: Game, date: String): Int {
        // Add the session object to the database mock
        // Increment the last identifier
        lastId++
        // Add the updated session object to the database mock
        db.add(
            // The session object to be added to the database mock
            // The only fields that are changed are:
            // - sid: The last identifier. This is the last identifier available in the database mock
            // - playersSession: An empty set. This is because the session is created with no players by default
            Session(
                lastId,
                capacity,
                date,
                game,
                emptySet()
            )
        )
        // Return the last identifier
        return lastId
    }

    override fun getById(id: Int): Session? {
        // Read the session object from the database mock with the given id
        return db.find { it.sid == id }
    }

    override fun getSessionsSearch(gid: Int, date: String?, state: String?, pid: Int?, limit : Int, skip: Int): List<Session> {
        // Get all the session objects from the database mock that match the given parameters
        // Start by checking the game identifier
        var sessions = db.filter { it.gameSession.gid == gid }
        // Then check the date
        date?.let {
            sessions = sessions.filter { it.date == date }
        }
        var tmp_state: String? = null
        // Then check the state
        state?.let {
            sessions = sessions.filter { tmp_state == state }
        }
        // Then check the player identifier in the players set
        pid?.let {
            sessions = sessions.filter { it.playersSession.any { it.pid == pid } }
        }
        return sessions.subList(skip, skip + limit)
    }

    override fun update(sid: Int, pid: Int) {
        // Update the session object in the database mock
        db.forEach { session ->
            // search for the session with the given id
            if (session.sid == sid) {
                // if found
                // remove the session from the database mock
                db.remove(session)
                // add the new session to the database mock
                db.add(
                    Session(
                        session.sid,
                        session.capacity,
                        session.date,
                        session.gameSession,
                        session.playersSession.plus(
                            session.playersSession.find { it.pid == pid } ?: throw SessionNotFoundException("Player not found")
                        )
                    )
                )
            }
        }
        // tell the caller that the update was not successful
        throw SessionNotFoundException("Session not found")
    }

    override fun delete(id: Int) {
        // Delete the session object from the database mock
        db.forEach {
            // search for the session with the given id
            if (it.sid == id) {
                // if found
                // remove the session from the database mock
                db.remove(it)
            }
        }
        // tell the caller that the delete was not successful
        throw SessionNotFoundException("Session not found")
    }
}