package pt.isel.ls.services

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.session.SESSION_MAX_CAPACITY
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.services.*
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.success

class SessionsService(val storage: SessionsDataManager) {

    // Sessions exceptions must be discussed
    fun createSession(capacity: UInt, gid: UInt, date: LocalDateTime): SessionCreationResult {

        if (capacity <= 1u || capacity > SESSION_MAX_CAPACITY) {
            return failure(SessionCreationException.InvalidCapacity)
        }

        val getGame = storage.game.getById(gid)

        val storageSession = storage.session

        return if (getGame == null) {
            failure(SessionCreationException.GameNotFound)
        } else {
            success(storageSession.create(capacity, getGame, date))
        }

    }

    fun addPlayer(sid: UInt, pid: UInt): SessionAddPlayerResult {

            val getSession = storage.session.getById(sid) ?: return failure(SessionAddPlayerException.SessionNotFound)

            if (getSession.capacity <= 1u || getSession.capacity > SESSION_MAX_CAPACITY) {
                 failure(SessionAddPlayerException.InvalidCapacity)
            }

            val getPlayer = storage.player.getById(pid)

            return if (getPlayer == null) {
                failure(SessionAddPlayerException.PlayerNotFound)
            } else {
                success(storage.session.update(sid, getPlayer))
            }

    }

    fun listSessions(gid: UInt, date: LocalDateTime?, state: State?, pid: UInt?, limit: UInt, skip: UInt): SessionSearchResult {

            return if (storage.session.getById(gid) == null) {
                failure(SessionSearchException.GameNotFound)
            } else if (pid != null && storage.player.getById(pid) == null) {
                failure(SessionSearchException.PLayerNotFound)
            } else {
                success(storage.session.getSessionsSearch(gid, date, state, pid, limit, skip))
            }

    }

    fun getSessionById(sid: UInt): SessionDetailsResult {

        val getSession = storage.session.getById(sid)

        return if (getSession == null) {
            failure(SessionDetailsException.SessionNotFound)
        } else {
            success(getSession)
        }

    }
}