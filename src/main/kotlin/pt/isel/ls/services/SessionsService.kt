package pt.isel.ls.services

import pt.isel.ls.data.domain.session.SESSION_MAX_CAPACITY
import pt.isel.ls.exceptions.services.*
import pt.isel.ls.storage.SessionsDataGame
import pt.isel.ls.storage.SessionsDataPlayer
import pt.isel.ls.storage.SessionsDataSession
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.isValidTimeStamp
import pt.isel.ls.utils.success

class SessionsService(
    val storageSession: SessionsDataSession,
    val storageGame: SessionsDataGame,
    val storagePlayer: SessionsDataPlayer
) {

    // Sessions exceptions must be discussed
    fun createSession(capacity: Int, gid: Int, date: String): SessionCreationResult {
        if (capacity <= 1 || capacity > SESSION_MAX_CAPACITY) {
            return failure(SessionCreationException.InvalidCapacity)
        }

        if (date.isNotBlank() && date.isValidTimeStamp()) {
            return failure(SessionCreationException.InvalidDate)
        }

        val getGame = storageGame.getById(gid)

        return if (getGame == null) {
            failure(SessionCreationException.GameNotFound)
        } else {
            success(storageSession.create(capacity, getGame, date))
        }

    }

    fun addPlayer(sid: Int, pid: Int): SessionAddPlayerResult {
        val getSession = storageSession.getById(sid) ?: return failure(SessionAddPlayerException.SessionNotFound)

        if (getSession.playersSession.size + 1 > SESSION_MAX_CAPACITY) {
            return failure(SessionAddPlayerException.SessionFull)
        }

        val getPlayer = storagePlayer.getById(pid)

        return if (getPlayer == null) {
            failure(SessionAddPlayerException.PlayerNotFound)
        } else {
            success(storageSession.update(sid, pid))
        }
    }

    fun listSessions(gid: Int, date: String?, state: String?, pid: Int?, limit: Int, skip: Int): SessionSearchResult {

        if (date != null) {
            if (date.isNotBlank() && date.isValidTimeStamp()) {
                return failure(SessionSearchException.InvalidDate)
            }
        }

        return if (storageGame.getById(gid) == null) {
            failure(SessionSearchException.GameNotFound)
        } else if (pid?.let { storagePlayer.getById(it) } == null) {
            failure(SessionSearchException.PLayerNotFound)
        } else {
            success(storageSession.getSessionsSearch(gid, date, state, pid, limit, skip))
        }
    }

    fun getSessionById(sid: Int): SessionDetailsResult {
        val getSession = storageSession.getById(sid)
        return if (getSession == null) {
            failure(SessionDetailsException.SessionNotFound)
        } else {
            success(getSession)
        }
    }
}