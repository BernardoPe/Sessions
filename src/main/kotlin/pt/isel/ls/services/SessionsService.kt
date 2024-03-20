package pt.isel.ls.services

import pt.isel.ls.domain.session.SESSION_MAX_CAPACITY
import pt.isel.ls.exceptions.services.*
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.isValidTimeStamp
import pt.isel.ls.utils.success

class SessionsService(val storage: SessionsDataManager) {

    // Sessions exceptions must be discussed
    fun createSession(capacity: Int, gid: Int, date: String): SessionCreationResult {
        if (capacity <= 1 || capacity > SESSION_MAX_CAPACITY) {
            return failure(SessionCreationException.InvalidCapacity)
        }

        if (date.isNotBlank() && date.isValidTimeStamp()) {
            return failure(SessionCreationException.InvalidDate)
        }

        return storage.apply {
            val getGame = it.storageGame.getById(gid)
            val storageSession = it.storageSession
            if (getGame == null) {
                failure(SessionCreationException.GameNotFound)
            } else {
                success(storageSession.create(capacity, getGame, date))
            }
        }

    }

    fun addPlayer(sid: Int, pid: Int): SessionAddPlayerResult {
        return storage.apply {
            val getSession =
                it.storageSession.getById(sid) ?: return@apply failure(SessionAddPlayerException.SessionNotFound)
            if (getSession.capacity <= 1 || getSession.capacity > SESSION_MAX_CAPACITY) {
                return@apply failure(SessionAddPlayerException.InvalidCapacity)
            }

            val getPlayer = it.storagePlayer.getById(pid)

            if (getPlayer == null) {
                failure(SessionAddPlayerException.PlayerNotFound)
            } else {
                success(it.storageSession.update(sid, pid))
            }
        }
    }

    fun listSessions(gid: Int, date: String?, state: String?, pid: Int?, limit: Int?, skip: Int?): SessionSearchResult {

        if (date != null) {
            if (date.isNotBlank() && date.isValidTimeStamp()) {
                return failure(SessionSearchException.InvalidDate)
            }
        }

        return storage.apply { storage ->
            if (storage.storageSession.getById(gid) == null) {
                failure(SessionSearchException.GameNotFound)
            } else if (pid?.let { storage.storagePlayer.getById(it) } == null) {
                failure(SessionSearchException.PLayerNotFound)
            } else {
                success(storage.storageSession.getSessionsSearch())
            }
        }
    }

    fun getSessionById(sid: Int): SessionDetailsResult {
        return storage.apply {
            val getSession = it.storageSession.getById(sid)
            if (getSession == null) {
                failure(SessionDetailsException.SessionNotFound)
            } else {
                success(getSession)
            }
        }
    }
}