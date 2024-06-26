package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.isBefore
import java.sql.Connection

/**
 * Service Handler for Session related operations
 *
 * Responsible for handling the business logic of the Session entity
 *
 * @property dataManager [SessionsDataManager] instance to access the data storage
 *
 */
class SessionsService(private val dataManager: SessionsDataManager) {

    private val sessionStorage = dataManager.session
    private val gameStorage = dataManager.game

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

        return dataManager.executeTransaction {

            val game = gameStorage.getById(gid) ?: throw NotFoundException("Game not found")

            val newSession = Session(0u, capacity, date, game, emptySet())

            sessionStorage.create(newSession)

        }

    }

    /**
     * Adds a player to a session
     *
     * @param sid The session identifier
     * @param pid The player identifier
     *
     * @throws NotFoundException If the session or player is not found
     * @throws BadRequestException If the player is already in the session, the session is full or closed
     */

    fun addPlayer(sid: UInt, pid: UInt) {

        dataManager.executeTransaction({

            val session = sessionStorage.getById(sid) ?: throw NotFoundException("Session not found")

            if (session.playersSession.any { it.id == pid }) {
                throw BadRequestException("Player already in session")
            }

            if (session.playersSession.size.toUInt() == session.capacity) {
                throw BadRequestException("Session is full")
            }

            if (session.state == State.CLOSE) {
                throw BadRequestException("Session is closed")
            }

            sessionStorage.addPlayer(sid, pid)

        }, Connection.TRANSACTION_SERIALIZABLE)

    }

    /**
     * Removes a player from a session
     *
     * @param sid The session identifier
     * @param pid The player identifier
     *
     * @throws NotFoundException If the session or player is not found or the player is not in the session
     */
    fun removePlayer(sid: UInt, pid: UInt) {

        return dataManager.executeTransaction {

            val session = sessionStorage.getById(sid) ?: throw NotFoundException("Session not found")

            if (session.playersSession.none { it.id == pid }) {
                throw NotFoundException("Player not in session")
            }

            sessionStorage.removePlayer(sid, pid)

        }

    }

    /**
     * Updates a session
     *
     * @param sid The session identifier
     * @param capacity The session capacity
     * @param date The session date
     *
     * @throws BadRequestException If the session date is in the past, the new session capacity is less than the number of players in the session or the new session capacity is invalid
     * @throws NotFoundException If the session is not found
     */

    fun updateSession(sid: UInt, capacity: UInt?, date: LocalDateTime?): Boolean {

        if (date != null && date.isBefore(currentLocalTime())) {
            throw BadRequestException("Session date must be in the future")
        }

        return dataManager.executeTransaction {

            val session = sessionStorage.getById(sid) ?: throw NotFoundException("Session not found")

            if (session.state == State.CLOSE) {
                throw BadRequestException("Session is closed")
            }

            val updatedSession = session.copy(capacity = capacity ?: session.capacity, date = date ?: session.date)

            sessionStorage.update(updatedSession)

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
        return dataManager.session.getSessionsSearch(gid, date, state, pid, limit, skip)
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
        return dataManager.session.getById(sid) ?: throw NotFoundException("Session not found")
    }


    /**
     * Deletes a session
     *
     * @param sid The session identifier
     *
     * @throws NotFoundException If the session is not found
     */
    fun deleteSession(sid: UInt) {
        if (!dataManager.session.delete(sid)) {
            throw NotFoundException("Session not found")
        }
    }
}

typealias SessionIdentifier = UInt

typealias SessionList = List<Session>

