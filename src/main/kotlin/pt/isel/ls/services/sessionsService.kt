package pt.isel.ls.services

import pt.isel.ls.domain.session.Session

class sessionsService {

    fun createSession(capacity: Int, gid: Int, date: String): Int {
        TODO()
    }

    fun addPlayer(sid: Int, pid: Int): String {
        TODO()
    }

    fun listSessions(gid: Int, date: String?, state: String?, pid: Int?, limit: Int?, skip: Int?): Set<Session> {
        TODO()
    }

    fun getSessionById(sid: Int): Session {
        TODO()
    }

    /** More methods to come */
}