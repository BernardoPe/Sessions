package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.ConflictException
import pt.isel.ls.exceptions.InternalServerErrorException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.isBefore

class SessionsService(val storage: SessionsDataManager) {

    fun createSession(capacity: UInt, gid: UInt, date: LocalDateTime): UInt {

        if (date.isBefore(currentLocalTime()))
            throw BadRequestException("Session date must be in the future")

        val getGame = storage.game.getById(gid) ?: throw NotFoundException("Game not found")

        val session = Session(0u, capacity, date, getGame, emptySet())

        return storage.session.create(session)

    }

    fun addPlayer(sid: UInt, pid: UInt): SessionAddPlayerMessage {

        val getSession = storage.session.getById(sid) ?: throw NotFoundException("Session not found")

        val getPlayer = storage.player.getById(pid) ?: throw NotFoundException("Player not found")

        if (getSession.playersSession.contains(getPlayer))
            throw ConflictException("Player already in session")

        if (getSession.capacity == getSession.playersSession.size.toUInt())
            throw BadRequestException("Session is full")

        if (getSession.state == State.CLOSE)
            throw BadRequestException("Session is closed")

        return if (storage.session.addPlayer(sid, getPlayer))
            "Player successfully added to session"
        else throw InternalServerErrorException()
        // If update fails after checks this means that something went wrong with the update, so we throw an internal server error

    }

    fun removePlayer(sid: UInt, pid: UInt): SessionAddPlayerMessage {

        val getSession = storage.session.getById(sid) ?: throw NotFoundException("Session not found")

        val getPlayer = storage.player.getById(pid) ?: throw NotFoundException("Player not found")

        if (!getSession.playersSession.contains(getPlayer))
            throw NotFoundException("Player not in session")

        return if (storage.session.removePlayer(sid, getPlayer.id))
            "Player successfully removed from session"
        else throw InternalServerErrorException()
        // If update fails after checks this means that something went wrong with the update, so we throw an internal server error

    }

    fun updateSession(sid: UInt, capacity: UInt?, date: LocalDateTime?): SessionAddPlayerMessage {

        if (date != null) {
            if (date.isBefore(currentLocalTime()))
                throw BadRequestException("Session date must be in the future")
        }

        val getSession = storage.session.getById(sid) ?: throw NotFoundException("Session not found")

        return if (storage.session.update(sid, capacity, date))
            "Session successfully updated"
        else throw InternalServerErrorException()
        // If update fails after checks this means that something went wrong with the update, so we throw an internal server error

    }

    fun listSessions(gid: UInt, date: LocalDateTime?, state: State?, pid: UInt?, limit: UInt, skip: UInt): SessionList {
        val sessionsSearch = storage.session.getSessionsSearch(gid, date, state, pid, limit, skip)
        return sessionsSearch.ifEmpty { throw NotFoundException("No sessions were found") }
    }


    fun getSessionById(sid: UInt): Session {
        return storage.session.getById(sid) ?: throw NotFoundException("Session not found")
    }

}

typealias SessionIdentifier = UInt

typealias SessionList = List<Session>

typealias SessionAddPlayerMessage = String