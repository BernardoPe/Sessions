import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlinx.datetime.LocalDateTime
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.storage.db.SessionsDataDBGame
import pt.isel.ls.storage.db.SessionsDataDBPlayer
import pt.isel.ls.storage.db.SessionsDataDBSession
import java.util.UUID

class SessionsDataDBSessionTest {

    private val TEST_EMAIL = "test@test.com"

    @Test
    fun `create session successfully`() {
        // Arrange
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        var game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game, emptySet())
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        // Game
        var game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game, emptySet())
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
        val sessionDB = SessionsDataDBSession()
        // Act
        val retrievedSession = sessionDB.getById(999u)
        // Assert
        assertNull(retrievedSession)
    }

    @Test
    fun `delete session by id`() {
        // Arrange
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        var game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game, emptySet())
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        // Game
        var game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game, emptySet())
        val sid = sessionDB.create(session)
        session = session.copy(id = sid)
        // Act
        sessionDB.update(sid, capacity = 20u, date = LocalDateTime(2025, 1, 2, 0, 0))
        val retrievedSession = sessionDB.getById(sid)
        // Assert
        assertEquals(session.copy(id = sid, capacity = 20u, date = LocalDateTime(2025, 1, 2, 0, 0)), retrievedSession)
        // Clean up
        gameDB.delete(gid)
        sessionDB.delete(sid)
    }

    @Test
    fun `update non-existent session`() {
        // Arrange
        val sessionDB = SessionsDataDBSession()
        // Act
        val isUpdated = sessionDB.update(999u, capacity = 20u, date = LocalDateTime(2025, 1, 2, 0, 0))
        // Assert
        assertFalse(isUpdated)
    }

    @Test
    fun `get all sessions`() {
        // Arrange
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        // Game
        var game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session1
        var session1 = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game, emptySet())
        val sid1 = sessionDB.create(session1)
        session1 = session1.copy(id = sid1)
        // Session2
        var session2 = Session(0u, 10u, LocalDateTime(2025, 1, 2, 0, 0), game, emptySet())
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        // Game
        var game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session1
        var session1 = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game, emptySet())
        val sid1 = sessionDB.create(session1)
        session1 = session1.copy(id = sid1)
        // Session2
        var session2 = Session(0u, 10u, LocalDateTime(2025, 1, 2, 0, 0), game, emptySet())
        val sid2 = sessionDB.create(session2)
        session2 = session2.copy(id = sid2)
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        var game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        val session1 = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game, emptySet())
        val session2 = Session(0u, 10u, LocalDateTime(2025, 1, 2, 0, 0), game, emptySet())
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        // Game 1
        var game1 = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        game1 = game1.copy(id = gid1)
        // Game2
        var game2 = Game(2u, "Test Game 2".toName(), "Test Developer 2".toName(), setOf(Genre("RPG")))
        val gid2 = gameDB.create(game2)
        game2 = game2.copy(id = gid2)
        // Session 1
        var session1 = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game1, emptySet())
        val sid1 = sessionDB.create(session1)
        session1 = session1.copy(id = sid1)
        var session2 = Session(0u, 10u, LocalDateTime(2025, 1, 2, 0, 0), game2, emptySet())
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        var game1 = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        var game2 = Game(2u, "Test Game 2".toName(), "Test Developer 2".toName(), setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game1, emptySet())
        val session2 = Session(0u, 10u, LocalDateTime(2025, 1, 2, 0, 0), game2, emptySet())
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        var game1 = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        var game2 = Game(2u, "Test Game 2".toName(), "Test Developer 2".toName(), setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game1, emptySet())
        val session2 = Session(0u, 10u, LocalDateTime(2025, 1, 2, 0, 0), game2, emptySet())
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        var game1 = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        var game2 = Game(2u, "Test Game 2".toName(), "Test Developer 2".toName(), setOf(Genre("RPG")))
        val gid1 = gameDB.create(game1)
        val gid2 = gameDB.create(game2)
        game1 = game1.copy(id = gid1)
        game2 = game2.copy(id = gid2)
        val session1 = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game1, emptySet())
        val session2 = Session(0u, 10u, LocalDateTime(2025, 1, 2, 0, 0), game2, emptySet())
        val sid1 = sessionDB.create(session1)
        val sid2 = sessionDB.create(session2)
        // Act
        val retrievedSessions = sessionDB.getSessionsSearch(game1.id, session1.date, null, null, limit = 10u, skip = 0u).first
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
        val gameDB = SessionsDataDBGame()
        val sessionDB = SessionsDataDBSession()
        val playerDB = SessionsDataDBPlayer()
        // Game
        var game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf(Genre("RPG")))
        val gid = gameDB.create(game)
        game = game.copy(id = gid)
        // Session
        var session = Session(0u, 10u, LocalDateTime(2025, 1, 1, 0, 0), game, emptySet())
        val sid = sessionDB.create(session)
        session = session.copy(id = sid)
        // Player
        var player = Player(0u, "Test Player".toName(), TEST_EMAIL.toEmail(), 10L)
        val pid = playerDB.create(player)
        player = player.copy(id = pid.first)
        // Add player to session
        sessionDB.addPlayer(sid, player)
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