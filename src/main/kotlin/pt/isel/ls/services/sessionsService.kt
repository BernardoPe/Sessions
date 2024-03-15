package pt.isel.ls.services

import pt.isel.ls.domain.session.Session
import pt.isel.ls.dto.session.SessionRequest
import pt.isel.ls.dto.session.SessionSearch

class sessionsService {

    fun createSession(session : SessionRequest) {
        TODO()
    }

    fun addPlayer(sid: Int, pid: Int) {
        TODO()
    }

    fun listSessions(session: SessionSearch, limit : Int, skip : Int) {
        TODO()
    }

    fun getSessionDetails(sid: Int): Session {
        TODO()
    }

    /** More methods to come */
}