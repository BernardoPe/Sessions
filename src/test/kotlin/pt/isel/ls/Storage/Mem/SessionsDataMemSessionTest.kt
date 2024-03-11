package pt.isel.ls.Storage.Mem

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import pt.isel.ls.DTO.Session.Session

class SessionsDataMemSessionTest {

    @Test
    fun testCreateAndReadSession() {
        // Create a session
        val session = Session(1, 5, 1, "2022/01/01")
        // Create a session storage
        val sessionStorage = SessionsDataMemSession()
        // Create the session (add it to the storage)
        sessionStorage.createSession(session)
        // Check if the session was created
        // compare the session with the session read from the storage
        assertEquals(session, sessionStorage.readSession(1))
        // Check the session data
        // Start by reading the session from the storage
        val sessionData = sessionStorage.readSession(1)
        // Check the session id
        assertEquals(1, sessionData?.ssid)
        // Check the session capacity
        assertEquals(5, sessionData?.capacity)
        // Check the session gid
        assertEquals(1, sessionData?.gid)
        // Check the session date
        assertEquals("2022/01/01", sessionData?.date)
        // Check if the session with id 2 was not created
        assertNull(sessionStorage.readSession(2))
    }

    @Test
    fun testUpdateSession() {
        // Create a session
        val session = Session(1, 5, 1, "2022/01/01")
        // Create a session storage
        val sessionStorage = SessionsDataMemSession()
        // Create the session (add it to the storage)
        sessionStorage.createSession(session)
        // Update the session
        val newSession = Session(1, 10, 2, "2022/01/02")
        sessionStorage.updateSession(1, newSession)
        // Check if the session was updated
        // compare the session with the session read from the storage
        assertEquals(newSession, sessionStorage.readSession(1))
        // Check the session data
        // Start by reading the session from the storage
        val sessionData = sessionStorage.readSession(1)
        // Check the session id
        assertEquals(1, sessionData?.ssid)
        // Check the session capacity
        assertEquals(10, sessionData?.capacity)
        // Check the session gid
        assertEquals(2, sessionData?.gid)
        // Check the session date
        assertEquals("2022/01/02", sessionData?.date)
    }

    @Test
    fun testDeleteSession() {
        // Create a session
        val session = Session(1, 5, 1, "2022/01/01")
        // Create a session storage
        val sessionStorage = SessionsDataMemSession()
        // Create the session (add it to the storage)
        sessionStorage.createSession(session)
        // Delete the session
        sessionStorage.deleteSession(0)
        // Check if the session was deleted
        assertNull(sessionStorage.readSession(0))
    }
}