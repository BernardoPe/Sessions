package pt.isel.ls.storage.db

import kotlinx.datetime.toKotlinLocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.utils.plus
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration

class SessionsDataDBSessionTest {

    private val testEmail = "test@test.com".toEmail()
    private val testName = "Test Name".toName()
    private val testName2 = "Test Name 2".toName()
    private val testDeveloper = "Test Dev".toName()
    private val testDeveloper2 = "Test Dev 2".toName()
    private val testDate = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS).toKotlinLocalDateTime()
    private val dbURL = System.getenv("JDBC_DEVELOPMENT_DATABASE_URL")

    @Test
    fun `create session successfully`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        var game = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session = Session(0u, 10u, testDate, game, emptySet())
        // Act
        val sid = sessionDB.create(session)
        val retrievedSession = sessionDB.getById(sid)
        // Assert
        assertEquals(session.copy(id = sid), retrievedSession)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid)
    }

    @Test
    fun `get session by id`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        // Game
        var game = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, testDate, game, emptySet())
        val sid = sessionDB.create(session)
        session = session.copy(id = sid)
        // Act
        val retrievedSession = sessionDB.getById(sid)
        // Assert
        assertEquals(session.copy(id = sid), retrievedSession)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid)
    }

    @Test
    fun `get non-existent session by id`() {
        // Arrange
        val sessionDB = SessionsDataDBSession(dbURL)
        // Act
        val retrievedSession = sessionDB.getById(999u)
        // Assert
        assertNull(retrievedSession)
    }

    @Test
    fun `delete session by id`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        var game = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session = Session(0u, 10u, testDate, game, emptySet())
        val sid = sessionDB.create(session)
        // Act
        sessionDB.delete(sid)
        val retrievedSession = sessionDB.getById(sid)
        // Assert
        assertNull(retrievedSession)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid)
    }

    @Test
    fun `update session successfully`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        // Game
        var game = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, testDate, game, emptySet())
        val sid = sessionDB.create(session)
        session = session.copy(id = sid)
        // Act
        val newSession = session.copy(capacity = 20u, date = testDate + Duration.parse("PT1H"))
        sessionDB.update(newSession)
        val retrievedSession = sessionDB.getById(sid)
        // Assert
        assertEquals(session.copy(id = sid, capacity = 20u, date = testDate + Duration.parse("PT1H")), retrievedSession)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid)
    }

    @Test
    fun `update non-existent session`() {
        // Arrange
        val sessionDB = SessionsDataDBSession(dbURL)
        // Act
        val session = Session(999u, 10u, testDate, Game(1u, testName, testDeveloper, setOf(Genre("RPG"))), emptySet())
        val isUpdated = sessionDB.update(session)
        // Assert
        assertFalse(isUpdated)
    }

    @Test
    fun `get all sessions`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        // Game
        var game = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session1
        var session1 = Session(0u, 10u, testDate, game, emptySet())
        val sid1 = sessionDB.create(session1)
        session1 = session1.copy(id = sid1)
        // Session2
        var session2 = Session(0u, 10u, testDate, game, emptySet())
        val sid2 = sessionDB.create(session2)
        session2 = session2.copy(id = sid2)
        // Act
        val retrievedSessions = sessionDB.getSessionsSearch(null, null, null, null, limit = 10u, skip = 0u).first
        // Assert
        assertEquals(listOf(session1.copy(id = sid1), session2.copy(id = sid2)), retrievedSessions)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid1)
        sessionDB.delete(sid2)
    }

    @Test
    fun `get all sessions with limit`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        // Game
        var game = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session1
        var session1 = Session(0u, 10u, testDate, game, emptySet())
        val sid1 = sessionDB.create(session1)
        session1 = session1.copy(id = sid1)
        // Session2
        val session2 = Session(0u, 10u, testDate, game, emptySet())
        val sid2 = sessionDB.create(session2)
        // Act
        val retrievedSessions = sessionDB.getSessionsSearch(null, null, null, null, limit = 1u, skip = 0u).first
        // Assert
        assertEquals(listOf(session1.copy(id = sid1)), retrievedSessions)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid1)
        sessionDB.delete(sid2)
    }

    @Test
    fun `get all sessions with skip`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        var game = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session1 = Session(0u, 10u, testDate, game, emptySet())
        val session2 = Session(0u, 10u, testDate, game, emptySet())
        val sid1 = sessionDB.create(session1)
        val sid2 = sessionDB.create(session2)
        // Act
        val retrievedSessions = sessionDB.getSessionsSearch(null, null, null, null, limit = 10u, skip = 1u).first
        // Assert
        assertEquals(listOf(session2.copy(id = sid2)), retrievedSessions)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid1)
        sessionDB.delete(sid2)
    }

    @Test
    fun `get all sessions with search`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        // Game 1
        var game1 = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        game1 = game1.copy(id = gid1)
        // Game2
        var game2 = Game(2u, testName2, testDeveloper2, setOf(Genre("RPG")))
        val gid2 = gameDB.create(game2)
        game2 = game2.copy(id = gid2)
        // Session 1
        var session1 = Session(0u, 10u, testDate, game1, emptySet())
        val sid1 = sessionDB.create(session1)
        session1 = session1.copy(id = sid1)
        var session2 = Session(0u, 10u, testDate, game2, emptySet())
        val sid2 = sessionDB.create(session2)
        session2 = session2.copy(id = sid2)
        // Act
        val retrievedSessions = sessionDB.getSessionsSearch(null, null, null, null, limit = 10u, skip = 0u).first
        // Assert
        assertEquals(listOf(session1.copy(id = sid1), session2.copy(id = sid2)), retrievedSessions)
        // Clean up
        gameDB.delete(gid1)
        gameDB.delete(gid2)
        sessionDB.delete(sid1)
        sessionDB.delete(sid2)
    }

    @Test
    fun `get all sessions with search and limit`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        var game1 = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        var game2 = Game(2u, testName2, testDeveloper2, setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, testDate, game1, emptySet())
        val session2 = Session(0u, 10u, testDate, game2, emptySet())
        val sid1 = sessionDB.create(session1)
        val sid2 = sessionDB.create(session2)
        // Act
        val retrievedSessions = sessionDB.getSessionsSearch(null, null, null, null, limit = 1u, skip = 0u).first
        // Assert
        assertEquals(listOf(session1.copy(id = sid1)), retrievedSessions)
        // Clean up
        gameDB.delete(gid1)
        gameDB.delete(gid2)
        sessionDB.delete(sid1)
        sessionDB.delete(sid2)
    }

    @Test
    fun `get all sessions with search and skip`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        var game1 = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        var game2 = Game(2u, testName2, testDeveloper2, setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, testDate, game1, emptySet())
        val session2 = Session(0u, 10u, testDate, game2, emptySet())
        val sid1 = sessionDB.create(session1)
        val sid2 = sessionDB.create(session2)
        // Act
        val retrievedSessions = sessionDB.getSessionsSearch(null, null, null, null, limit = 10u, skip = 1u).first
        // Assert
        assertEquals(listOf(session2.copy(id = sid2)), retrievedSessions)
        // Clean up
        gameDB.delete(gid1)
        gameDB.delete(gid2)
        sessionDB.delete(sid1)
        sessionDB.delete(sid2)
    }

    @Test
    fun `get all sessions with search with gid and date`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        var game1 = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        var game2 = Game(2u, testName2, testDeveloper2, setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, testDate, game1, emptySet())
        val session2 = Session(0u, 10u, testDate, game2, emptySet())
        val sid1 = sessionDB.create(session1)
        val sid2 = sessionDB.create(session2)
        // Act
        val retrievedSessions =
            sessionDB.getSessionsSearch(game1.id, session1.date, null, null, limit = 10u, skip = 0u).first
        // Assert
        assertEquals(listOf(session1.copy(id = sid1)), retrievedSessions)
        // Clean up
        gameDB.delete(gid1)
        gameDB.delete(gid2)
        sessionDB.delete(sid1)
        sessionDB.delete(sid2)
    }

    @Test
    fun `remove player from session`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        val playerDB = SessionsDataDBPlayer(dbURL)
        // Game
        var game = Game(1u, testName, testDeveloper, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, testDate, game, emptySet())
        val sid = sessionDB.create(session)
        session = session.copy(id = sid)
        // Player
        var player = Player(0u, testName, testEmail, PasswordHash("testPassword"))
        val pid = playerDB.create(player)
        player = player.copy(id = pid.first)
        // Add player to session
        sessionDB.addPlayer(sid, player.id)
        // Act
        sessionDB.removePlayer(sid, pid.first)
        val retrievedSession = sessionDB.getById(sid)
        // Assert
        assertEquals(session.copy(id = sid), retrievedSession)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid)
        playerDB.delete(pid.first)
    }

}