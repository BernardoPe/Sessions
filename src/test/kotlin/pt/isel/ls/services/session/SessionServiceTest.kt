package pt.isel.ls.services.session

import org.junit.jupiter.api.BeforeEach
import org.mindrot.jbcrypt.BCrypt
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.domain.primitives.Password
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.data.domain.session.SESSION_MAX_CAPACITY
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.MemManager
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.plus
import java.util.*
import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class SessionServiceTest {
    @Test
    fun testCreateSession_Success() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        assertNotNull(createdSession)

        val getSession = serviceSession.getSessionById(createdSession)

        val getGame = serviceGame.getGameById(gid)

        assertEquals(
            Session(createdSession, capacity, date, getGame, emptySet()),
            getSession,
        )
    }

    @Test
    fun testCreateSession_GameNotFound() {
        val capacity = newTestCapacity()
        val gid = Random.nextUInt()
        val date = newTestDateTime()

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.createSession(capacity, gid, date)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Game not found", exception.errorCause)
    }

    private fun testHashPassword(s: String): String {
        return BCrypt.hashpw(s, BCrypt.gensalt(12))
    }

    @Test
    fun testAddPlayer_Success() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword"))
        val hashed = testHashPassword("TestPassword")

        assertNotNull(player)

        serviceSession.addPlayer(createdSession, player.first)

        val getSession = serviceSession.getSessionById(createdSession)

        assertEquals(
            Player(player.first, playerName, playerEmail, PasswordHash(hashed)),
            getSession.playersSession.first(),
        )

    }

    @Test
    fun testAddPlayer_SessionNotFound() {
        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword"))

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.addPlayer(Random.nextUInt(), player.first)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Session not found", exception.errorCause)
    }

    @Test
    fun testAddPlayer_PlayerNotFound() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.addPlayer(createdSession, Random.nextUInt())
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Player not found", exception.errorCause)
    }

    @Test
    fun testAddPlayer_PlayerAlreadyInSession() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword"))

        assertNotNull(player)

        serviceSession.addPlayer(createdSession, player.first)

        val exception = assertFailsWith<BadRequestException> {
            serviceSession.addPlayer(createdSession, player.first)
        }

        assertEquals("Bad Request", exception.description)
        assertEquals("Player already in session", exception.errorCause)
        assertEquals(400, exception.status)
    }

    @Test
    fun testAddPlayer_SessionIsFull() {
        val capacity = 2u
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val playerName1 = newTestPlayerName().toName()
        val playerEmail1 = newTestEmail().toEmail()

        val playerName2 = newTestPlayerName().toName()
        val playerEmail2 = newTestEmail().toEmail()

        val playerName3 = newTestPlayerName().toName()
        val playerEmail3 = newTestEmail().toEmail()

        val player1 = servicePlayer.createPlayer(playerName1, playerEmail1, Password("TestPassword"))
        val player2 = servicePlayer.createPlayer(playerName2, playerEmail2, Password("TestPassword"))
        val player3 = servicePlayer.createPlayer(playerName3, playerEmail3, Password("TestPassword"))

        assertNotNull(player1)
        assertNotNull(player2)

        serviceSession.addPlayer(createdSession, player1.first)
        serviceSession.addPlayer(createdSession, player2.first)

        val exception = assertFailsWith<BadRequestException> {
            serviceSession.addPlayer(createdSession, player3.first)
        }

        assertEquals("Bad Request", exception.description)
        assertEquals("Session is full", exception.errorCause)
    }

    @Test
    fun testAddPlayer_SessionIsClosed() {
        val capacity = 2u
        val game = Game(0u, "Game Name".toName(), "Developer".toName(), setOf(Genre("RPG")))
        val gid = serviceGame.createGame(game.name, game.developer, game.genres)
        val date = currentLocalTime() + 500.milliseconds

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword"))

        assertNotNull(player)

        while (currentLocalTime() <= date);

        val exception = assertFailsWith<BadRequestException> {
            serviceSession.addPlayer(createdSession, player.first)
        }

        assertEquals("Bad Request", exception.description)
        assertEquals("Session is closed", exception.errorCause)
    }

    @Test
    fun testRemovePlayer_Success() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword"))

        assertNotNull(player)

        serviceSession.addPlayer(createdSession, player.first)

        serviceSession.removePlayer(createdSession, player.first)

        val getSession = serviceSession.getSessionById(createdSession)

        assertEquals(emptySet(), getSession.playersSession)

    }

    @Test
    fun testRemovePlayer_SessionNotFound() {
        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword"))

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.removePlayer(Random.nextUInt(), player.first)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Session not found", exception.errorCause)
    }

    @Test
    fun testRemovePlayer_PlayerNotFound() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.removePlayer(createdSession, Random.nextUInt())
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Player not in session", exception.errorCause)
    }

    @Test
    fun testRemovePlayer_PlayerNotInSession() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword"))

        assertNotNull(player)

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.removePlayer(createdSession, player.first)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Player not in session", exception.errorCause)
    }

    @Test
    fun testUpdateSession_Success() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val newCapacity = newTestCapacity()
        val newDate = newTestDateTime()

        serviceSession.updateSession(createdSession, newCapacity, newDate)

        val getSession = serviceSession.getSessionById(createdSession)

        val getGame = serviceGame.getGameById(gid)

        assertEquals(
            Session(createdSession, newCapacity, newDate, getGame, emptySet()),
            getSession,
        )

    }

    @Test
    fun `test update new session capacity smaller than players in session`() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val player1 = servicePlayer.createPlayer(newTestPlayerName().toName(), newTestEmail().toEmail(), Password("TestPassword")).first
        val player2 = servicePlayer.createPlayer(newTestPlayerName().toName(), newTestEmail().toEmail(), Password("TestPassword")).first

        serviceSession.addPlayer(createdSession, player1)
        serviceSession.addPlayer(createdSession, player2)

        val newCapacity = 1u
        val newDate = newTestDateTime()

        val updatedSession = assertFailsWith<IllegalArgumentException> {
            serviceSession.updateSession(createdSession, newCapacity, newDate)
        }

        assertEquals("Session capacity must not be less than the number of players", updatedSession.message)
    }

    @Test
    fun `test update new session date in the past`() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val newCapacity = newTestCapacity()
        val newDate = currentLocalTime()

        Thread.sleep(100)

        val updatedSession = assertFailsWith<BadRequestException> {
            serviceSession.updateSession(createdSession, newCapacity, newDate)
        }

        assertEquals("Bad Request", updatedSession.description)
        assertEquals("Session date must be in the future", updatedSession.errorCause)
    }

    @Test
    fun `test update session invalid capacity`() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        val newCapacity = 0u
        val newDate = newTestDateTime()

        val updatedSession = assertFailsWith<IllegalArgumentException> {
            serviceSession.updateSession(createdSession, newCapacity, newDate)
        }

        assertEquals("Session capacity must be at least 1 and at most $SESSION_MAX_CAPACITY", updatedSession.message)
    }

    @Test
    fun `test delete session, session not found`() {
        val exception = assertFailsWith<NotFoundException> {
            serviceSession.deleteSession(Random.nextUInt())
        }
        assertEquals("Not Found", exception.description)
        assertEquals("Session not found", exception.errorCause)
    }

    @Test
    fun `test delete session, session deleted`() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val gid = serviceGame.createGame(gameName, developer, genres)
        val date = newTestDateTime()

        val createdSession = serviceSession.createSession(capacity, gid, date)

        serviceSession.deleteSession(createdSession)

        assertNull(storage.session.getById(createdSession))

    }

    @Test
    fun testUpdateSession_SessionNotFound() {
        val newCapacity = newTestCapacity()
        val newDate = newTestDateTime()

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.updateSession(Random.nextUInt(), newCapacity, newDate)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Session not found", exception.errorCause)
    }

    @Test
    fun testSessionsSearch_Success() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val date = newTestDateTime()
        val limit = 5u
        val skip = 0u

        val createdGame = serviceGame.createGame(gameName, developer, genres)

        assertNotNull(createdGame)

        val createdSession1 = serviceSession.createSession(capacity, createdGame, date)
        val createdSession2 = serviceSession.createSession(capacity, createdGame, date)

        val sessionSearched = serviceSession.listSessions(createdGame, date, State.OPEN, null, limit, skip)

        val getGame = serviceGame.getGameById(createdGame)

        assertEquals(
            listOf(
                Session(createdSession1, capacity, date, getGame, emptySet()),
                Session(createdSession2, capacity, date, getGame, emptySet()),
            ),
            sessionSearched.first,
        )
        assertEquals(2, sessionSearched.second)
    }

    @Test
    fun testSessionsSearch_GameNotFound() {
        val date = newTestDateTime()
        val limit = 5u
        val skip = 0u

        val randomGameId = Random.nextUInt()

        val sessions = serviceSession.listSessions(randomGameId, date, State.OPEN, null, limit, skip)

        assertTrue { sessions.first.isEmpty() }
        assertEquals(0, sessions.second)
    }

    @Test
    fun testGetSessionDetails_Success() {
        val capacity = newTestCapacity()
        val gameName = newTestGameName().toName()
        val developer = newTestDeveloper().toName()
        val genres = newTestGenres()
        val date = newTestDateTime()

        val createdGame = serviceGame.createGame(gameName, developer, genres)

        val createdSession = serviceSession.createSession(capacity, createdGame, date)

        assertNotNull(createdSession)

        val getSession = serviceSession.getSessionById(createdSession)

        val getGame = serviceGame.getGameById(createdGame)

        assertEquals(
            Session(createdSession, capacity, date, getGame, emptySet()),
            getSession,
        )
    }

    @Test
    fun testGetSessionDetails_SessionNotFound() {
        val randomId = Random.nextUInt()

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.getSessionById(randomId)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Session not found", exception.errorCause)
    }

    @BeforeEach
    fun clearStorage() {
        storage.close()
        serviceSession = SessionsService(storage)
        serviceGame = GameService(storage)
        servicePlayer = PlayerService(storage)
    }

    companion object {
        private fun newTestCapacity() = Random.nextInt(10, 100).toUInt()

        private fun newTestGameName() = "Game Name Test ${Random.nextUInt()}"

        private fun newTestDeveloper() = "Developer Test ${Random.nextUInt()}"

        private fun newTestGenres() = setOf(Genre("RPG"), Genre("Adventure"))

        private fun newTestPlayerName() = "player-${abs(Random.nextLong())}"

        private fun newTestEmail() = "email-${abs(Random.nextLong())}@test.com"

        private fun newTestDateTime() = currentLocalTime() + Random.nextLong(10, 1000).seconds

        private fun UUID.testTokenHash() = mostSignificantBits xor leastSignificantBits

        private var storage = MemManager()

        private var serviceSession = SessionsService(storage)

        private var serviceGame = GameService(storage)

        private var servicePlayer = PlayerService(storage)
    }
}
