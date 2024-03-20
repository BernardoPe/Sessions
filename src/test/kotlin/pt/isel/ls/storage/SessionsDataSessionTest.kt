package pt.isel.ls.storage

import org.junit.jupiter.api.Assertions.assertEquals
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.storage.mem.SessionsDataMemSession
import kotlin.test.Test


class SessionsDataSessionTest {

    @Test
    fun createTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        storage.create(1, Game(0, "game", "developer", setOf("genre1", "genre2")), "2021-05-05 12:00:00")
        // Check if the session was added
        // Get the session from the storage
        val session = storage.getById(0)
        // Check each field of the session
        // Check the capacity
        assertEquals(1, session?.capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0, session?.gameSession?.gid)
        // Check the name
        assertEquals("game", session?.gameSession?.name)
        // Check the developer
        assertEquals("developer", session?.gameSession?.developer)
        // Check the genres
        assertEquals(setOf("genre1", "genre2"), session?.gameSession?.genres)
        // Check the date
        assertEquals("2021-05-05 12:00:00", session?.date)
    }

    @Test
    fun getByIdTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        storage.create(1, Game(0, "game", "developer", setOf("genre1", "genre2")), "2021-05-05 12:00:00")
        // Get the session from the storage
        val session = storage.getById(0)
        // Check each field of the session
        // Check the capacity
        assertEquals(1, session?.capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0, session?.gameSession?.gid)
        // Check the name
        assertEquals("game", session?.gameSession?.name)
        // Check the developer
        assertEquals("developer", session?.gameSession?.developer)
        // Check the genres
        assertEquals(setOf("genre1", "genre2"), session?.gameSession?.genres)
        // Check the date
        assertEquals("2021-05-05 12:00:00", session?.date)
    }

    @Test
    fun getSessionsSearchJustByGidAndDateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        storage.create(1, Game(0, "game", "developer", setOf("genre1", "genre2")), "2021-05-05 12:00:00")
        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(0, "2021-05-05 12:00:00", null, null, 10, 0)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(1, sessions[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0, sessions[0].gameSession.gid)
        // Check the name
        assertEquals("game", sessions[0].gameSession.name)
        // Check the developer
        assertEquals("developer", sessions[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("genre1", "genre2"), sessions[0].gameSession.genres)
        // Check the date
        assertEquals("2021-05-05 12:00:00", sessions[0].date)
    }

    @Test
    fun getSessionsSearchJustByGidAndPidTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        storage.create(1, Game(0, "game", "developer", setOf("genre1", "genre2")), "2021-05-05 12:00:00")
        // Add a player to the session
        storage.update(0, Player(0, "player", "testemail@test.com"))
        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(0, null, null, 0, 10, 0)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(1, sessions[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0, sessions[0].gameSession.gid)
        // Check the name
        assertEquals("game", sessions[0].gameSession.name)
        // Check the developer
        assertEquals("developer", sessions[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("genre1", "genre2"), sessions[0].gameSession.genres)
        // Check the date
        assertEquals("2021-05-05 12:00:00", sessions[0].date)
    }

    @Test
    fun updateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        storage.create(1, Game(0, "game", "developer", setOf("genre1", "genre2")), "2021-05-05 12:00:00")
        // Add a player to the session
        storage.update(0, Player(0, "player", "testemail@test.com"))
        // Get the session from the storage
        val session = storage.getById(0)
        // Check if the player was added
        // Check the number of players
        assertEquals(1, session?.playersSession?.size)
        // Check the player identifier
        assertEquals(0, session?.playersSession?.first()!!.pid)
    }

    @Test
    fun deleteTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        storage.create(1, Game(0, "game", "developer", setOf("genre1", "genre2")), "2021-05-05 12:00:00")
        // Delete the session from the storage
        storage.delete(0)
        // Get the session from the storage
        val session = storage.getById(0)
        // Check if the session was deleted
        assertEquals(null, session)
    }

    @Test
    fun getSessionsSearchJustByGidTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        storage.create(1, Game(0, "game", "developer", setOf("genre1", "genre2")), "2021-05-05 12:00:00")
        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(0, null, null, null, 10, 0)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(1, sessions[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0, sessions[0].gameSession.gid)
        // Check the name
        assertEquals("game", sessions[0].gameSession.name)
        // Check the developer
        assertEquals("developer", sessions[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("genre1", "genre2"), sessions[0].gameSession.genres)
        // Check the date
        assertEquals("2021-05-05 12:00:00", sessions[0].date)
    }

    @Test
    fun getSessionsSearchJustByDateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        storage.create(1, Game(0, "game", "developer", setOf("genre1", "genre2")), "2021-05-05 12:00:00")
        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(0, "2021-05-05 12:00:00", null, null, 10, 0)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(1, sessions[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0, sessions[0].gameSession.gid)
        // Check the name
        assertEquals("game", sessions[0].gameSession.name)
        // Check the developer
        assertEquals("developer", sessions[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("genre1", "genre2"), sessions[0].gameSession.genres)
        // Check the date
        assertEquals("2021-05-05 12:00:00", sessions[0].date)
    }
}