
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.storage.db.SessionsDataDBGame
import pt.isel.ls.storage.db.SessionsDataDBPlayer
import pt.isel.ls.storage.db.SessionsDataDBSession
import java.util.*

class SessionsDataDBSessionTest {

    private val TEST_EMAIL = "test@test.com".toEmail()
    private val TEST_NAME = "Test Name".toName()
    private val TEST_NAME_2 = "Test Name 2".toName()
    private val TEST_DEVELOPER = "Test Dev".toName()
    private val TEST_DEVELOPER_2 = "Test Dev 2".toName()
    private val TEST_DATE = LocalDateTime(2025, 1, 1, 0, 0)

    private val dbURL = System.getenv("JDBC_DEVELOPMENT_DATABASE_URL")

    @Test
    fun `create session successfully`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        var game = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session = Session(0u, 10u, TEST_DATE, game, emptySet())
        // Act
        val sid = sessionDB.create(session.capacity, session.date, session.gameSession.id)
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
        var game = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid = sessionDB.create(session.capacity, session.date, session.gameSession.id)
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
        var game = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid = sessionDB.create(session.capacity, session.date, session.gameSession.id)
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
        var game = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid = sessionDB.create(session.capacity, session.date, session.gameSession.id)
        session = session.copy(id = sid)
        // Act
        sessionDB.update(sid, capacity = 20u, date = TEST_DATE)
        val retrievedSession = sessionDB.getById(sid)
        // Assert
        assertEquals(session.copy(id = sid, capacity = 20u, date = TEST_DATE), retrievedSession)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid)
    }

    @Test
    fun `update non-existent session`() {
        // Arrange
        val sessionDB = SessionsDataDBSession(dbURL)
        // Act
        val isUpdated = sessionDB.update(999u, capacity = 20u, date = TEST_DATE)
        // Assert
        assertFalse(isUpdated)
    }

    @Test
    fun `get all sessions`() {
        // Arrange
        val gameDB = SessionsDataDBGame(dbURL)
        val sessionDB = SessionsDataDBSession(dbURL)
        // Game
        var game = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session1
        var session1 = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid1 = sessionDB.create(session1.capacity, session1.date, session1.gameSession.id)
        session1 = session1.copy(id = sid1)
        // Session2
        var session2 = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid2 = sessionDB.create(session2.capacity, session2.date, session2.gameSession.id)
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
        var game = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session1
        var session1 = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid1 = sessionDB.create(session1.capacity, session1.date, session1.gameSession.id)
        session1 = session1.copy(id = sid1)
        // Session2
        val session2 = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid2 = sessionDB.create(session2.capacity, session2.date, session2.gameSession.id)
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
        var game = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session1 = Session(0u, 10u, TEST_DATE, game, emptySet())
        val session2 = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid1 = sessionDB.create(session1.capacity, session1.date, session1.gameSession.id)
        val sid2 = sessionDB.create(session2.capacity, session2.date, session2.gameSession.id)
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
        var game1 = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        game1 = game1.copy(id = gid1)
        // Game2
        var game2 = Game(2u, TEST_NAME_2, TEST_DEVELOPER_2, setOf(Genre("RPG")))
        val gid2 = gameDB.create(game2)
        game2 = game2.copy(id = gid2)
        // Session 1
        var session1 = Session(0u, 10u, TEST_DATE, game1, emptySet())
        val sid1 = sessionDB.create(session1.capacity, session1.date, session1.gameSession.id)
        session1 = session1.copy(id = sid1)
        var session2 = Session(0u, 10u, TEST_DATE, game2, emptySet())
        val sid2 = sessionDB.create(session2.capacity, session2.date, session2.gameSession.id)
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
        var game1 = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        var game2 = Game(2u, TEST_NAME_2, TEST_DEVELOPER_2, setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, TEST_DATE, game1, emptySet())
        val session2 = Session(0u, 10u, TEST_DATE, game2, emptySet())
        val sid1 = sessionDB.create(session1.capacity, session1.date, session1.gameSession.id)
        val sid2 = sessionDB.create(session2.capacity, session2.date, session2.gameSession.id)
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
        var game1 = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        var game2 = Game(2u, TEST_NAME_2, TEST_DEVELOPER_2, setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, TEST_DATE, game1, emptySet())
        val session2 = Session(0u, 10u, TEST_DATE, game2, emptySet())
        val sid1 = sessionDB.create(session1.capacity, session1.date, session1.gameSession.id)
        val sid2 = sessionDB.create(session2.capacity, session2.date, session2.gameSession.id)
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
        var game1 = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        var game2 = Game(2u, TEST_NAME_2, TEST_DEVELOPER_2, setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, TEST_DATE, game1, emptySet())
        val session2 = Session(0u, 10u, TEST_DATE, game2, emptySet())
        val sid1 = sessionDB.create(session1.capacity, session1.date, session1.gameSession.id)
        val sid2 = sessionDB.create(session2.capacity, session2.date, session2.gameSession.id)
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
        var game = Game(1u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, TEST_DATE, game, emptySet())
        val sid = sessionDB.create(session.capacity, session.date, session.gameSession.id)
        session = session.copy(id = sid)
        // Player
        var player = Player(0u, TEST_NAME, TEST_EMAIL, 10L)
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

    private fun UUID.hash() = leastSignificantBits xor mostSignificantBits
}