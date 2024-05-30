package pt.isel.ls.storage.mem

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataSession

/**
 *  SessionsDataMemSession
 *
 *  Session Data management class
 *
 *  Uses the [SessionsDataSession] class to manage the session data
 *
 */

class SessionsDataMemSession : SessionsDataSession, MemoryStorage() {

    override fun create(session: Session): UInt {
        // Add the session object to the database mock
        sessionDB.add(
            // The session object to be added to the database mock
            // The only fields that are changed are:
            // - sid: The last identifier. This is the last identifier available in the database mock
            // - playersSession: An empty set. This is because the session is created with no players by default
            Session(
                sid,
                session.capacity,
                session.date,
                session.gameSession,
                emptySet()
            ),
        )
        // Return the last identifier
        return sid - 1u
    }

    override fun getById(id: UInt): Session? {
        // Read the session object from the database mock with the given id
        return sessionDB.find { it.id == id }
    }

    override fun getSessionsSearch(gid: UInt?, date: LocalDateTime?, state: State?, pid: UInt?, limit: UInt, skip: UInt): Pair<List<Session>, Int> {
        // Get all the session objects from the database mock that match the given parameters
        // Start by checking the game identifier
        var sessions = sessionDB.toList()

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

    override fun addPlayer(sid: UInt, pid: UInt): Boolean {

        val player = playerDB.find { it.id == pid } ?: throw NotFoundException("Player not found")

        // Update the session object in the database mock
        sessionDB.forEachIndexed { index, session ->
            // search for the session with the given id
            if (session.id == sid) {
                // if found
                // remove the session from the database mock
                sessionDB.removeAt(index)
                // add the new session to the database mock
                sessionDB.add(
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

        val getPlayer = playerDB.find { it.id == pid } ?: throw NotFoundException("Player not found")

        sessionDB.forEachIndexed { index, session ->
            // search for the session with the given id
            if (session.id == sid) {
                // if found
                // remove the session from the database mock
                sessionDB.removeAt(index)
                // add the new session to the database mock
                sessionDB.add(
                    Session(
                        session.id,
                        session.capacity,
                        session.date,
                        session.gameSession,
                        session.playersSession.minus(getPlayer),
                    ),
                )
                return true
            }
        }
        return false
    }

    override fun update(value: Session): Boolean {
        // Update the session object in the database mock
        sessionDB.forEachIndexed { index, session ->
            // search for the session with the given id
            if (session.id == value.id) {
                // if found
                // remove the session from the database mock
                sessionDB.removeAt(index)
                // add the new session to the database mock
                // the new session has the same fields as the old session,
                // except for the capacity and date fields, which are updated
                sessionDB.add(
                    Session(
                        session.id,
                        value.capacity,
                        value.date,
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
        sessionDB.forEachIndexed { index, it ->
            // search for the session with the given id
            if (it.id == id) {
                // if found
                // remove the session from the database mock
                sessionDB.removeAt(index)
                return true
            }
        }
        return false
    }

    override fun clear() {
        // Clear the database mock
        sessionDB.clear()
    }

}
