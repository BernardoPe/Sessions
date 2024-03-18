package pt.isel.ls.services

import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.storage.SessionsDataSession

class sessionsService(val storage: SessionsDataSession) {

    fun createSession(capacity: Int, gid: Int, date: String): SessionIdentifier {
        TODO()
    }

    fun addPlayer(sid: Int, pid: Int): SessionAddPlayerResult {
        TODO()
    }

    fun listSessions(gid: Int, date: String?, state: String?, pid: Int?, limit: Int?, skip: Int?): SessionSearchResult {
        TODO()
    }

    fun getSessionById(sid: Int): Session {
        TODO()
    }

    /** More methods to come */
}

typealias SessionIdentifier = Int

typealias SessionSearchResult = Set<Session>

typealias SessionAddPlayerResult = String