package pt.isel.ls.services.session

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.data.domain.toEmail
import pt.isel.ls.data.domain.toName
import pt.isel.ls.domain.session.SessionTest
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.ConflictException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import pt.isel.ls.utils.toLocalDateTime
import java.util.*
import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

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
            getSession
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

        val player = servicePlayer.createPlayer(playerName, playerEmail)

        assertNotNull(player)

        val addedPlayer = serviceSession.addPlayer(createdSession, player.first)

        val getSession = serviceSession.getSessionById(createdSession)

        assertEquals(
            Player(player.first, playerName, playerEmail, player.second.testTokenHash()),
            getSession.playersSession.first()
        )

        assertEquals("Player successfully added to session", addedPlayer)
    }

    @Test
    fun testAddPlayer_SessionNotFound() {
        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = servicePlayer.createPlayer(playerName, playerEmail)

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

        val player = servicePlayer.createPlayer(playerName, playerEmail)

        assertNotNull(player)

        serviceSession.addPlayer(createdSession, player.first)

        val exception = assertFailsWith<ConflictException> {
            serviceSession.addPlayer(createdSession, player.first)
        }

        assertEquals("Conflict", exception.description)
        assertEquals("Player already in session", exception.errorCause)
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

        val player1 = servicePlayer.createPlayer(playerName1, playerEmail1)
        val player2 = servicePlayer.createPlayer(playerName2, playerEmail2)
        val player3 = servicePlayer.createPlayer(playerName3, playerEmail3)

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

//    @Test
//    fun testAddPlayer_SessionIsClosed() {
//        val capacity = 2u
//        val game = newTestGame()
//        val gid = serviceGame.createGame(game.name, game.developer, game.genres)
//        val date = currentLocalTime() + 2.seconds
//
//        val createdSession = serviceSession.createSession(capacity, gid, date)
//
//        val playerName = newTestPlayerName().toName()
//        val playerEmail = newTestEmail().toEmail()
//
//        val player = servicePlayer.createPlayer(playerName, playerEmail)
//
//        assertNotNull(player)
//
//        Thread.sleep(3000)
//
//        val exception = assertFailsWith<BadRequestException> {
//            serviceSession.addPlayer(createdSession, player.first)
//        }
//
//        assertEquals("Bad Request", exception.description)
//        assertEquals("Session is closed", exception.errorCause)
//    }

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
                Session(createdSession2, capacity, date, getGame, emptySet())
            ),
            sessionSearched
        )
    }

    @Test
    fun testSessionsSearch_GameNotFound() {
        val date = newTestDateTime()
        val limit = 5u
        val skip = 0u

        val randomGameId = Random.nextUInt()

        val exception = assertFailsWith<NotFoundException> {
            serviceSession.listSessions(randomGameId, date, State.OPEN, null, limit, skip)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Game not found", exception.errorCause)
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
            getSession
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

    companion object {
        private fun newTestCapacity() = Random.nextInt(1, 100).toUInt()

        private fun newTestGameName() = "Game Name Test ${Random.nextUInt()}"

        private fun newTestDeveloper() = "Developer Test ${Random.nextUInt()}"

        private fun newTestGenres() = setOf(Genre("RPG"), Genre("Adventure"))

        private fun newTestPlayerName() = "player-${abs(Random.nextLong())}"

        private fun newTestEmail() = "email-${abs(Random.nextLong())}@test.com"

        private fun newTestDateTime() = "2024-05-05T12:00:00".toLocalDateTime()

        private fun UUID.testTokenHash() = mostSignificantBits xor leastSignificantBits

        private fun newPlayersTest() = setOf(
            Player(1u, "player1".toName(), "player1@example.com".toEmail(), SessionTest.uuid),
            Player(2u, "player2".toName(), "player2@example.com".toEmail(), SessionTest.uuid),
            Player(3u, "player3".toName(), "player3@example.com".toEmail(), SessionTest.uuid),
            Player(4u, "player4".toName(), "player4@example.com".toEmail(), SessionTest.uuid),
            Player(5u, "player5".toName(), "player5@example.com".toEmail(), SessionTest.uuid),
            Player(6u, "player6".toName(), "player6@example.com".toEmail(), SessionTest.uuid)
        )

        private val storage =
            SessionsDataManager(SessionsDataMemGame(), SessionsDataMemPlayer(), SessionsDataMemSession())

        private val serviceSession = SessionsService(storage)

        private val serviceGame = GameService(storage)

        private val servicePlayer = PlayerService(storage)

    }

}