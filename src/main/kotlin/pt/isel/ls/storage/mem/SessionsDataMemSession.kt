package pt.isel.ls.storage.mem

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.storage.SessionsDataSession

/**
 *  SessionsDataMemSession
 *
 *  Session Data management class
 *
 *  Uses the [SessionsDataSession] class to manage the session data
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
    private var lastId = 1u

    override fun create(session: Session): UInt {
        // Add the session object to the database mock
        // Increment the last identifier
        // Add the updated session object to the database mock
        db.add(
            // The session object to be added to the database mock
            // The only fields that are changed are:
            // - sid: The last identifier. This is the last identifier available in the database mock
            // - playersSession: An empty set. This is because the session is created with no players by default
            Session(
                lastId,
                session.capacity,
                session.date,
                session.gameSession,
                session.playersSession,
            ),
        )
        // Return the last identifier
        return lastId++
    }

    override fun getById(id: UInt): Session? {
        // Read the session object from the database mock with the given id
        return db.find { it.id == id }
    }

    override fun getSessionsSearch(gid: UInt?, date: LocalDateTime?, state: State?, pid: UInt?, limit: UInt, skip: UInt): Pair<List<Session>, Int> {
        // Get all the session objects from the database mock that match the given parameters
        // Start by checking the game identifier
        var sessions = db.toList()

        gid?.let {
            sessions = sessions.filter { it.gameSession.id == gid }
        }
        // Then check the date

        date?.let {
            sessions = sessions.filter { it.date == date }
        }

        // Then check the state
        state?.let {
            sessions = sessions.filter { it.state == state }
        }
        // Then check the player identifier in the players set
        pid?.let {
            sessions = sessions.filter { it.playersSession.any { it.id == pid } }
        }

        sessions = sessions.sortedBy { it.id }

        return Pair(sessions.drop(skip.toInt()).take(limit.toInt()), sessions.size)

    }

    override fun addPlayer(sid: UInt, player: Player): Boolean {
        // Update the session object in the database mock
        db.forEach { session ->
            // search for the session with the given id
            if (session.id == sid) {
                // if found
                // remove the session from the database mock
                db.remove(session)
                // add the new session to the database mock
                db.add(
                    Session(
                        session.id,
                        session.capacity,
                        session.date,
                        session.gameSession,
                        session.playersSession.plus(player),
                    ),
                )
                return true
            }
        }
        return false
    }

    override fun removePlayer(sid: UInt, pid: UInt): Boolean {
        // Remove the player from the session object in the database mock
        db.forEach { session ->
            // search for the session with the given id
            if (session.id == sid) {
                // if found
                // remove the session from the database mock
                db.remove(session)
                // add the new session to the database mock
                // the new session has the same fields as the old session,
                // except for the playersSession field, which has the player removed
                db.add(
                    Session(
                        session.id,
                        session.capacity,
                        session.date,
                        session.gameSession,
                        session.playersSession.filter { it.id != pid }.toSet(),
                    ),
                )
                return true
            }
        }
        return false
    }

    override fun update(sid: UInt, capacity: UInt, date: LocalDateTime): Boolean {
        // Update the session object in the database mock
        db.forEach { session ->
            // search for the session with the given id
            if (session.id == sid) {
                // if found
                // remove the session from the database mock
                db.remove(session)
                // add the new session to the database mock
                db.add(
                    Session(
                        session.id,
                        capacity,
                        date,
                        session.gameSession,
                        session.playersSession,
                    ),
                )
                return true
            }
        }
        return false
    }

    override fun delete(id: UInt): Boolean {
        // Delete the session object from the database mock
        db.forEach {
            // search for the session with the given id
            if (it.id == id) {
                // if found
                // remove the session from the database mock
                db.remove(it)
                return true
            }
        }
        // tell the caller that the delete was not successful
        return false
    }
}
