package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Session.Session

class SessionsDataMemSession : SessionsDataMem<Session>() {
    fun createSession(Session: Session) {
        create(Session)
    }

    fun readSession(id: Int): Session? {
        return read(id)
    }

    fun updateSession(id: Int, session: Session) {
        update(id, session)
    }

    fun deleteSession(id: Int) {
        delete(id)
    }
}