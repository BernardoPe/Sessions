package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.session.SESSION_MAX_CAPACITY
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.isBefore

/**
 * Service Handler for Session related operations
 *
 * Responsible for handling the business logic of the Session entity
 *
 * @property storage [SessionsDataManager] instance to access the data storage
 *
 */
class SessionsService(val storage: SessionsDataManager) {

    /**
     * Creates a new session
     *
     * @param capacity The session capacity
     * @param gid The game identifier
     * @param date The session date
     * @return The [SessionIdentifier]
     *
     * @throws BadRequestException If the session date is in the past
     * @throws NotFoundException If the game is not found
     */
    fun createSession(capacity: UInt, gid: UInt, date: LocalDateTime): UInt {
        if (date.isBefore(currentLocalTime())) {
            throw BadRequestException("Session date must be in the future")
        }

        val getGame = storage.game.getById(gid) ?: throw NotFoundException("Game not found")

        val session = Session(0u, capacity, date, getGame, emptySet())

        return storage.session.create(session)
    }

    /**
     * Adds a player to a session
     *
     * @param sid The session identifier
     * @param pid The player identifier
     * @return The [SessionOperationMessage]
     *
     * @throws NotFoundException If the session or player is not found
     * @throws BadRequestException If the player is already in the session, the session is full or closed
     */

    fun addPlayer(sid: UInt, pid: UInt): SessionOperationMessage {
        val getSession = storage.session.getById(sid) ?: throw NotFoundException("Session not found")

        val getPlayer = storage.player.getById(pid) ?: throw NotFoundException("Player not found")

        if (getSession.playersSession.any { it.id == getPlayer.id }) {
            throw BadRequestException("Player already in session")
        }

        if (getSession.capacity == getSession.playersSession.size.toUInt()) {
            throw BadRequestException("Session is full")
        }

        if (getSession.state == State.CLOSE) {
            throw BadRequestException("Session is closed")
        }

        storage.session.addPlayer(sid, getPlayer).also {
            return "Player successfully added to session"
        }
    }

    /**
     * Removes a player from a session
     *
     * @param sid The session identifier
     * @param pid The player identifier
     * @return The [SessionOperationMessage]
     *
     * @throws NotFoundException If the session or player is not found or the player is not in the session
     */
    fun removePlayer(sid: UInt, pid: UInt): SessionOperationMessage {
        val getSession = storage.session.getById(sid) ?: throw NotFoundException("Session not found")

        val getPlayer = storage.player.getById(pid) ?: throw NotFoundException("Player not found")

        if (!getSession.playersSession.any { it.id == getPlayer.id }) {
            throw NotFoundException("Player not in session")
        }

        storage.session.removePlayer(sid, getPlayer.id).also {
            return "Player successfully removed from session"
        }
    }



    /**
     * Updates a session
     *
     * @param sid The session identifier
     * @param capacity The session capacity
     * @param date The session date
     * @return The [SessionOperationMessage]
     *
     * @throws BadRequestException If the session date is in the past, the new session capacity is less than the number of players in the session or the new session capacity is invalid
     * @throws NotFoundException If the session is not found
     */

    fun updateSession(sid: UInt, capacity: UInt?, date: LocalDateTime?): Boolean {

        if (date != null) {
            if (date.isBefore(currentLocalTime())) {
                throw BadRequestException("Session date must be in the future")
            }
        }

        val session = storage.session.getById(sid) ?: throw NotFoundException("Session not found")

        if (capacity != null) {
            if (session.playersSession.size > capacity.toInt()) {
                throw BadRequestException("New session capacity must be greater or equal to the number of players in the session")
            }
            if (capacity !in (1u..SESSION_MAX_CAPACITY)) {
                throw BadRequestException("Session capacity must at least 1 and at most $SESSION_MAX_CAPACITY")
            }
        }

        storage.session.update(sid, capacity, date).also {
            return true
        }
    }

    /**
     * Lists sessions
     *
     * @param gid The game identifier
     * @param date The session date
     * @param state The session state
     * @param pid The player identifier
     * @param limit The maximum number of sessions to return
     * @param skip The number of sessions to skip
     * @return The [SessionList] and the total number of sessions
     */
    fun listSessions(gid: UInt?, date: LocalDateTime?, state: State?, pid: UInt?, limit: UInt, skip: UInt): Pair<SessionList, Int> {
        val sessionsSearch = storage.session.getSessionsSearch(gid, date, state, pid, limit, skip)
        return sessionsSearch.first to sessionsSearch.second
    }


    /**
     * Gets a session by its identifier
     *
     * @param sid The session identifier
     * @return The [Session] instance
     *
     * @throws NotFoundException If the session is not found
     */

    fun getSessionById(sid: UInt): Session {
        return storage.session.getById(sid) ?: throw NotFoundException("Session not found")
    }


    /**
     * Deletes a session
     *
     * @param sid The session identifier
     * @return The [SessionOperationMessage]
     *
     * @throws NotFoundException If the session is not found
     */
    fun deleteSession(sid: UInt): SessionOperationMessage {
        if (storage.session.getById(sid) == null) {
            throw NotFoundException("Session not found")
        }
        storage.session.delete(sid).also {
            return "Session successfully deleted"
        }
    }
}

typealias SessionIdentifier = UInt

typealias SessionList = List<Session>

typealias SessionOperationMessage = String
