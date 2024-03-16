package pt.isel.ls.storage

import pt.isel.ls.domain.game.Game
import pt.isel.ls.domain.player.Player
import pt.isel.ls.domain.session.Session
import pt.isel.ls.storage.mem.SessionsDataMemSession
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SessionsDataGameSessionTest {

    @Test
    fun testCreateAndReadSession() {
        // Create a session
        val session = Session(1, 5, "2022-01-01 15:00:00", newGameTest(), newPlayersTest1())
        // Create a session storage
        val sessionStorage = SessionsDataMemSession()
        // Create the session (add it to the storage)
        sessionStorage.create(session)
        // Check if the session was created
        // compare the session with the session read from the storage
        assertEquals(session, sessionStorage.getById(1))
        // Check the session data
        // Start by reading the session from the storage
        val sessionData = sessionStorage.getById(1)
        // Check the session id
        assertEquals(1, sessionData?.sid)
        // Check the session capacity
        assertEquals(5, sessionData?.capacity)
        // Check the session gid
        assertEquals(1, sessionData?.gameSession?.gid)
        // Check the session date
        assertEquals("2022-01-01 15:00:00", sessionData?.date)
        // Check if the session with id 2 was not created
        assertNull(sessionStorage.getById(2))
    }

    @Test
    fun testAddNewPlayersSession() {
        // Create a session
        val session = Session(1, 5, "2022-01-01 15:00:00", newGameTest(), newPlayersTest1())
        // Create a session storage
        val sessionStorage = SessionsDataMemSession()
        // Create the session (add it to the storage)
        sessionStorage.create(session)
        // Update the session
        val newSession = Session(1, 10, "2022-01-02 15:00:00", newGameTest(), newPlayersTest1() + newPlayersTest2())
        sessionStorage.update(newSession.sid, newSession)
        // Check if the session was updated
        // compare the session with the session read from the storage
        assertEquals(newSession, sessionStorage.getById(1))
        // Check the session data
        // Start by reading the session from the storage
        val sessionData = sessionStorage.getById(1)
        // Check the session id
        assertEquals(1, sessionData?.sid)
        // Check the session capacity
        assertEquals(10, sessionData?.capacity)
        // Check the session gid
        assertEquals(
            Game(
                1,
                "Test Game 1",
                "Test Developer",
                setOf("Genre1", "Genre2")
            ), sessionData?.gameSession
        )
        // Check the session date
        assertEquals("2022-01-02 15:00:00", sessionData?.date)
        assertEquals(
            setOf(
                Player(1, "player1", "player1@example.com"),
                Player(2, "player2", "player2@example.com"),
                Player(3, "player3", "player3@example.com"),
                Player(4, "player4", "player4@example.com"),
                Player(5, "player5", "player5@example.com"),
                Player(6, "player6", "player6@example.com"),
            ), sessionData?.playersSession
        )
    }

    @Test
    fun testDeleteSession() {
        // Create a session
        val session = Session(1, 5, "2022-01-01 15:00:00", newGameTest(), newPlayersTest1())
        // Create a session storage
        val sessionStorage = SessionsDataMemSession()
        // Create the session (add it to the storage)
        sessionStorage.create(session)
        // Delete the session
        sessionStorage.delete(0)
        // Check if the session was deleted
        assertNull(sessionStorage.getById(0))
    }

    companion object {
        fun newGameTest() = Game(
            1,
            "Test Game 1",
            "Test Developer",
            setOf("Genre1", "Genre2")
        )

        fun newPlayersTest1() = setOf(
            Player(1, "player1", "player1@example.com"),
            Player(2, "player2", "player2@example.com"),
            Player(3, "player3", "player3@example.com"),
        )

        fun newPlayersTest2() = setOf(
            Player(4, "player4", "player4@example.com"),
            Player(5, "player5", "player5@example.com"),
            Player(6, "player6", "player6@example.com"),
        )
    }
}