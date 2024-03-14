package pt.isel.ls.Services

import pt.isel.ls.DTO.Session.Session
import pt.isel.ls.DTO.Session.SessionRequest
import pt.isel.ls.DTO.Session.SessionSearch

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