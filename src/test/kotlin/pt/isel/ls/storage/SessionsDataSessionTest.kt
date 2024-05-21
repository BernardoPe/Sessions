package pt.isel.ls.storage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.mapper.toGenre
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemSession
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.plus
import pt.isel.ls.utils.toLocalDateTime
import kotlin.test.Test
import kotlin.time.Duration

class SessionsDataSessionTest {

    @Test
    fun createTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock.capacity, sessionMock.date, sessionMock.gameSession.id) // Add a player to the session
        // Get the session from the storage
        val session = storage.getById(1u)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, session?.capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(1u, session?.gameSession?.id)
        // Check the name
        assertEquals("game".toName(), session?.gameSession?.name)
        // Check the developer
        assertEquals("developer".toName(), session?.gameSession?.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), session?.gameSession?.genres)
        // Check the date
        assertEquals(testDate, session?.date)
    }

    @Test
    fun getByIdTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock.capacity, sessionMock.date, sessionMock.gameSession.id)
        val session = storage.getById(1u)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, session?.capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(1u, session?.gameSession?.id)
        // Check the name
        assertEquals("game".toName(), session?.gameSession?.name)
        // Check the developer
        assertEquals("developer".toName(), session?.gameSession?.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), session?.gameSession?.genres)
        // Check the date
        assertEquals(testDate, session?.date)
    }

    @Test
    fun getSessionsSearchJustByGidAndDateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val session = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(session.capacity, session.date, session.gameSession.id)
        val sessions = storage.getSessionsSearch(1u, testDate, null, null, 10u, 0u)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.first.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, sessions.first[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(1u, sessions.first[0].gameSession.id)
        // Check the name
        assertEquals("game".toName(), sessions.first[0].gameSession.name)
        // Check the developer
        assertEquals("developer".toName(), sessions.first[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), sessions.first[0].gameSession.genres)
        // Check the date
        assertEquals(testDate, sessions.first[0].date)

        assertEquals(1, sessions.second)
    }

    @Test
    fun getSessionsSearchJustByGidAndPidTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val session = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(session.capacity, session.date, session.gameSession.id)
        storage.addPlayer(1u, 1u)
        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(1u, null, null, null, 10u, 0u)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.first.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, sessions.first[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(1u, sessions.first[0].gameSession.id)
        // Check the name
        assertEquals("game".toName(), sessions.first[0].gameSession.name)
        // Check the developer
        assertEquals("developer".toName(), sessions.first[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), sessions.first[0].gameSession.genres)
        // Check the date
        assertEquals(testDate, sessions.first[0].date)

        assertEquals(1, sessions.second)
    }

    @Test
    fun addPlayerTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock.capacity, sessionMock.date, sessionMock.gameSession.id) // Add a player to the session
        storage.addPlayer(1u, 1u)
        // Get the session from the storage
        val session = storage.getById(1u)
        // Check if the player was added
        // Check the number of players
        assertEquals(1, session?.playersSession?.size)
        // Check the player identifier
        assertEquals(1u, session?.playersSession?.first()!!.id)
    }

    @Test
    fun removePlayerTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock.capacity, sessionMock.date, sessionMock.gameSession.id) // Add a player to the session
        storage.addPlayer(1u, 1u)
        storage.removePlayer(1u, 1u)
        // Get the session from the storage
        val session = storage.getById(1u)
        // Check if the player was removed
        // Check the number of players
        assertEquals(0, session?.playersSession?.size)
        // Check the player identifier
        assertEquals(null, session?.playersSession?.firstOrNull())
    }

    @Test
    fun updateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock.capacity, sessionMock.date, sessionMock.gameSession.id)
        // Update the session with a new capacity, date
        storage.update(1u, 3u, "2030-05-06T12:00:00".toLocalDateTime())
        // Get the session from the storage
        val session = storage.getById(1u)
        // Check if the session was updated
        // Check the capacity
        assertEquals(3u, session?.capacity)
        // Check the date
        assertEquals("2030-05-06T12:00:00".toLocalDateTime(), session?.date)
    }

    @Test
    fun updateWithNoChangesTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock.capacity, sessionMock.date, sessionMock.gameSession.id)
        // Update the session with the same capacity and date
        storage.update(1u, 2u, testDate)
        // Get the session from the storage
        val session = storage.getById(1u)
        // Check if the session was updated
        // Check the capacity
        assertEquals(2u, session?.capacity)
        // Check the date
        assertEquals(testDate, session?.date)
    }

    @Test
    fun deleteTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val sessionMock = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(sessionMock.capacity, sessionMock.date, sessionMock.gameSession.id)
        storage.delete(1u)
        // Get the session from the storage
        val session = storage.getById(1u)
        // Check if the session was deleted
        assertEquals(null, session)
    }

    @Test
    fun getSessionsSearchJustByGidTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val session = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(session.capacity, session.date, session.gameSession.id)
        val sessions = storage.getSessionsSearch(1u, null, null, null, 10u, 0u)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.first.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, sessions.first[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(1u, sessions.first[0].gameSession.id)
        // Check the name
        assertEquals("game".toName(), sessions.first[0].gameSession.name)
        // Check the developer
        assertEquals("developer".toName(), sessions.first[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), sessions.first[0].gameSession.genres)
        // Check the date
        assertEquals(testDate, sessions.first[0].date)

        assertEquals(1, sessions.second)
    }

    @Test
    fun getSessionsSearchJustByDateTest() {
        // Create a session storage
        val storage = SessionsDataMemSession()
        // Add a session to the storage
        val session = Session(1u, 2u, testDate, Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())), emptySet())
        storage.create(session.capacity, session.date, session.gameSession.id)
        // Get the sessions from the storage
        val sessions = storage.getSessionsSearch(1u, testDate, null, null, 10u, 0u)
        // Check if the session was added
        // Check the number of sessions
        assertEquals(1, sessions.first.size)
        // Check each field of the session
        // Check the capacity
        assertEquals(2u, sessions.first[0].capacity)
        // Check each field of the game
        // Check the identifier
        assertEquals(1u, sessions.first[0].gameSession.id)
        // Check the name
        assertEquals("game".toName(), sessions.first[0].gameSession.name)
        // Check the developer
        assertEquals("developer".toName(), sessions.first[0].gameSession.developer)
        // Check the genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), sessions.first[0].gameSession.genres)
        // Check the date
        assertEquals(testDate, sessions.first[0].date)

        assertEquals(1, sessions.second)
    }

    companion object {
        val testDate = currentLocalTime() + Duration.parse("P1D")
    }

    @BeforeEach
    fun setup() {
        val gameStorage = SessionsDataMemGame()
        gameStorage.create(Game(1u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre())))
    }
    @BeforeEach
    fun clear() {
        SessionsDataMemSession().clear()
        SessionsDataMemGame().clear()
    }
}
