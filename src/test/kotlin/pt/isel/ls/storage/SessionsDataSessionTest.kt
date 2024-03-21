package pt.isel.ls.storage

import pt.isel.ls.data.domain.session.Session
import org.junit.jupiter.api.Assertions.assertEquals
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.toEmail
import pt.isel.ls.data.domain.toGenre
import pt.isel.ls.data.domain.toName
import pt.isel.ls.storage.mem.SessionsDataMemSession
import pt.isel.ls.utils.toLocalDateTime
import java.util.*
import kotlin.test.Test


class SessionsDataSessionTest {

    @Test
    fun createTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(0u, 2u, "2030-05-05T12:00:00".toLocalDateTime(), Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock) // Add a player to the session
        // Get the session from the storage
        val session = storage.getById(0u)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, session?.capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0u, session?.gameSession?.id)
        // Check the name
        assertEquals("game".toName(), session?.gameSession?.name)
        // Check the developer
        assertEquals("developer".toName(), session?.gameSession?.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), session?.gameSession?.genres)
        // Check the date
        assertEquals("2030-05-05T12:00:00".toLocalDateTime(), session?.date)
    }

    @Test
    fun getByIdTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(0u, 2u, "2030-05-05T12:00:00".toLocalDateTime(), Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock) // Add a player to the session
        val session = storage.getById(0u)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, session?.capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0u, session?.gameSession?.id)
        // Check the name
        assertEquals("game".toName(), session?.gameSession?.name)
        // Check the developer
        assertEquals("developer".toName(), session?.gameSession?.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), session?.gameSession?.genres)
        // Check the date
        assertEquals("2030-05-05T12:00:00".toLocalDateTime(), session?.date)
    }

    @Test
    fun getSessionsSearchJustByGidAndDateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val session = Session(0u, 2u, "2030-05-05T12:00:00".toLocalDateTime(), Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(session)        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(0u, "2030-05-05T12:00:00".toLocalDateTime(), null, null, 10u, 0u)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, sessions[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0u, sessions[0].gameSession.id)
        // Check the name
        assertEquals("game".toName(), sessions[0].gameSession.name)
        // Check the developer
        assertEquals("developer".toName(), sessions[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), sessions[0].gameSession.genres)
        // Check the date
        assertEquals("2030-05-05T12:00:00".toLocalDateTime(), sessions[0].date)
    }

    @Test
    fun getSessionsSearchJustByGidAndPidTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val session = Session(0u, 2u, "2030-05-05T12:00:00".toLocalDateTime(), Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(session)        // Add a player to the session
        storage.update(0u, Player(0u, "player".toName(), "testemail@test.com".toEmail(), UUID.randomUUID()))
        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(0u, null, null, 0u, 10u, 0u)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, sessions[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0u, sessions[0].gameSession.id)
        // Check the name
        assertEquals("game".toName(), sessions[0].gameSession.name)
        // Check the developer
        assertEquals("developer".toName(), sessions[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), sessions[0].gameSession.genres)
        // Check the date
        assertEquals("2030-05-05T12:00:00".toLocalDateTime(), sessions[0].date)
    }

    @Test
    fun updateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(0u, 2u, "2030-05-05T12:00:00".toLocalDateTime(), Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock) // Add a player to the session
        storage.update(0u, Player(0u, "player".toName(), "testemail@test.com".toEmail(), UUID.randomUUID()))
        // Get the session from the storage
        val session = storage.getById(0u)
        // Check if the player was added
        // Check the number of players
        assertEquals(1, session?.playersSession?.size)
        // Check the player identifier
        assertEquals(0u, session?.playersSession?.first()!!.id)
    }

    @Test
    fun deleteTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(0u, 2u, "2030-05-05T12:00:00".toLocalDateTime(), Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock) // Add a player to the session
        storage.delete(0u)
        // Get the session from the storage
        val session = storage.getById(0u)
        // Check if the session was deleted
        assertEquals(null, session)
    }

    @Test
    fun getSessionsSearchJustByGidTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val session = Session(0u, 2u, "2030-05-05T12:00:00".toLocalDateTime(), Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(session)        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(0u, null, null, null, 10u, 0u)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, sessions[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0u, sessions[0].gameSession.id)
        // Check the name
        assertEquals("game".toName(), sessions[0].gameSession.name)
        // Check the developer
        assertEquals("developer".toName(), sessions[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), sessions[0].gameSession.genres)
        // Check the date
        assertEquals("2030-05-05T12:00:00".toLocalDateTime(), sessions[0].date)
    }

    @Test
    fun getSessionsSearchJustByDateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val session = Session(0u, 2u, "2030-05-05T12:00:00".toLocalDateTime(), Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(session)
        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(0u, "2030-05-05T12:00:00".toLocalDateTime(), null, null, 10u, 0u)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, sessions[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(0u, sessions[0].gameSession.id)
        // Check the name
        assertEquals("game".toName(), sessions[0].gameSession.name)
        // Check the developer
        assertEquals("developer".toName(), sessions[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), sessions[0].gameSession.genres)
        // Check the date
        assertEquals("2030-05-05T12:00:00".toLocalDateTime(), sessions[0].date)
    }
}