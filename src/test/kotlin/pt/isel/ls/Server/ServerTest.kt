package pt.isel.ls.Server

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import pt.isel.ls.api.SessionsApi
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.dto.*
import pt.isel.ls.pt.isel.ls.SessionsServer
import pt.isel.ls.services.gameService
import pt.isel.ls.services.playerService
import pt.isel.ls.services.sessionsService
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import pt.isel.ls.data.domain.session.Session


/**

class ServerTest {


    @Test
    fun `invalid content-type should give bad request`(){
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "text/plain")
            .body("")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `not supported route should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/testroutenotsupported")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `method not implemented should give not allowed`() {
        // Arrange
        val request = Request(Method.HEAD, "/players")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.METHOD_NOT_ALLOWED)
    }

    @Test
    fun `test create player should create player`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"Test"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create player empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"","email":""}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }


    @Test
    fun `test create player, invalid email`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","email":"Test.com"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create player, player with email already exists`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","email":"TestEmail@gmail.com"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create player with invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test",""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test get player details should give player details`() {
        // Arrange
        val request = Request(Method.GET, "/players/1")
        // Act
        val response = server.sessionsHandler(request)
        val playerDetailsJson = response.bodyString()
        val playerDetails = Json.decodeFromString<PlayerInfoOutputModel>(playerDetailsJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(playerDetails.name == "TestName")
        assert(playerDetails.email == "TestEmail")
        assert(playerDetails.pid == 1)
    }
    @Test
    fun `test get player details not found`() {
        // Arrange
        val request = Request(Method.GET, "/players/3")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }
    @Test
    fun `test create game should create game`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test2","developer":"Test","genres":["Test"]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create game, game with name already exists`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","developer":"Test","genres":["Test"]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create game empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"","developer":"Test","genres":[""]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test create game with invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create game no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","developer":"Test","genres":["Test"]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.UNAUTHORIZED)
    }
    @Test
    fun `test get game details should give game details`() {
        // Arrange
        val request = Request(Method.GET, "/games/1")
        // Act
        val response = server.sessionsHandler(request)
        val gameDetailsJson = response.bodyString()
        val gameDetails = Json.decodeFromString<GameInfoOutputModel>(gameDetailsJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(gameDetails.name == "TestName")
        assert(gameDetails.developer == "TestDeveloper")
        assert(gameDetails.genres == listOf("TestGenre"))
        assert(gameDetails.gid == 1)
    }
    @Test
    fun `test get game details not found`() {
        // Arrange
        val request = Request(Method.GET, "/games/3")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `test get game list should give games`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"Test","genres":["TestGenre1"]}""")
        // Act
        val response = server.sessionsHandler(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<GameSearchOutputModel>(gameListJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(gameList.games.size == 2)
        assert(gameList.games[0].name == "TestName1")
        assert(gameList.games[0].developer == "TestDeveloper1")
        assert(gameList.games[0].genres == listOf("TestGenre1"))
        assert(gameList.games[0].gid == 1)
        assert(gameList.games[1].name == "TestName2")
        assert(gameList.games[1].developer == "TestDeveloper2")
        assert(gameList.games[1].genres == listOf("TestGenre2"))
        assert(gameList.games[1].gid == 2)
    }

    @Test
    fun `test get game list empty fields should give empty array`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"","genres":[]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(response.bodyString() == """{"games":[]}""")
    }
    @Test
    fun `test get game list invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test get game list limit and skip should give game list`() {
        // Arrange
        val request = Request(Method.GET, "/games?limit=1&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"developer":"Test","genres":["TestGenre1"]""")
        // Act
        val response = server.sessionsHandler(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<GameSearchOutputModel>(gameListJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(gameList.games.size == 1)
        assert(gameList.games[0].name == "TestName2")
        assert(gameList.games[0].developer == "TestDeveloper2")
        assert(gameList.games[0].genres == listOf("TestGenre2"))
        assert(gameList.games[0].gid == 2)
    }

    @Test
    fun `test create session should create session`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"2021-05-01 00:00:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create session exceeding max capacity supported should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"1000","date":"2021-05-01 00:00:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create session incorrect date format should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"1000","date":"0:2340:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"2021-05-01T00:00:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.UNAUTHORIZED)
    }

    @Test
    fun `test create session game not found`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":10,"capacity":"100","date":"2021-05-01T00:00:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `test create session with invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"description":"Test"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create session empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"","date":""}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session should add player`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.status == Status.OK)
    }

    @Test
    fun `add player to session player already in session`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":2}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.UNAUTHORIZED)
    }
    @Test
    fun `add player to session, session not found`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/10")
            .header("Content-Type", "application/json")
            .body("""{"pid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `add player to session, player not found`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":10}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `add player to session, empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":""}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session, invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":1""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `get session details should give session details`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1")
            .header("Content-Type", "application/json")
        // Act
        val response = server.sessionsHandler(request)
        val sessionBodyString = response.bodyString()
        val session = Json.decodeFromString<SessionInfoOutputModel>(sessionBodyString)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(session.gameSession.gid == 1)
        assert(session.capacity == 100)
        assert(session.date == "2021-05-01T00:00:00")
        assert(session.sid == 1)
    }

    @Test
    fun `get session details not found`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/10")
            .header("Content-Type", "application/json")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `test get session list should give session list`() {
        // Arrange
        val request = Request(Method.GET, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"date":"2021-05-01 00:00:00","state":"open","pid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchOutputModel>(sessionListJson)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(sessionList.sessions.size == 1)
        assert(sessionList.sessions[0].gameSession.gid == 1)
        assert(sessionList.sessions[0].capacity == 100)
        assert(sessionList.sessions[0].date == "2021-05-01T00:00:00")
        assert(sessionList.sessions[0].sid == 1)
    }

    @Test
    fun `test get session list empty fields should give empty array`() {
        // Arrange
        val request = Request(Method.GET, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.status == Status.BAD_REQUEST)
        assert(response.header("Content-Type") == "application/json")
        assert(response.bodyString() == """{"sessions":[]}""")
    }
    @Test
    fun `test get session list invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/sessions")
            .header("Content-Type", "application/json")
            .body("")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test get session list limit and skip should give session list`() {
        // Arrange
        val request = Request(Method.GET, "/sessions?limit=1&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"gid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchOutputModel>(sessionListJson)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(sessionList.sessions.size == 2)
        assert(sessionList.sessions[0].gameSession.gid == 1)
        assert(sessionList.sessions[0].capacity == 100)
        assert(sessionList.sessions[0].date == "2021-05-01T00:00:00")
        assert(sessionList.sessions[0].sid == 1)
        assert(sessionList.sessions[1].gameSession.gid == 1)
        assert(sessionList.sessions[1].capacity == 100)
        assert(sessionList.sessions[1].date == "2021-06-01T00:00:00")
        assert(sessionList.sessions[1].sid == 2)
    }

    companion object {

        private val playerStorage = SessionsDataMemPlayer()
        private val gameStorage = SessionsDataMemGame()
        private val sessionStorage = SessionsDataMemSession()

        private val server = SessionsServer(
            SessionsApi(
                playerService(playerStorage),
                gameService(gameStorage),
                sessionsService(sessionStorage)
        ))

        @JvmStatic
        @BeforeAll
        fun `create mocks and start server`(): Unit {

            val mockPlayer = Player(1, "TestName", "TestEmail")
            val mockPlayer2 = Player(2, "TestName2", "TestEmail@gmail.com")
            val mockGame1 = Game(1, "TestName", "TestDeveloper", setOf("TestGenre"))
            val mockGame2 = Game(2, "TestName2", "TestDeveloper2", setOf("TestGenre"))
            val mockSession = Session(1, 100, "2021-05-01T00:00:00", mockGame1, setOf())
            val mockSession2 = Session(2, 100, "2021-06-01T00:00:00", mockGame1, setOf())

            playerStorage.create(mockPlayer)
            playerStorage.create(mockPlayer2)
            gameStorage.create(mockGame1)
            gameStorage.create(mockGame2)
            sessionStorage.create(mockSession)
            sessionStorage.create(mockSession2)

            server.start()
        }

        @JvmStatic
        @AfterAll
        fun stopServer(): Unit {
            server.stop()
        }

    }


}*/