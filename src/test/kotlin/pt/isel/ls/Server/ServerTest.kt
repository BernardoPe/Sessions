package pt.isel.ls.Server

import kotlinx.serialization.json.Json
import org.http4k.core.*
import org.junit.After
import org.junit.Before
import pt.isel.ls.DTO.Game.Game
import pt.isel.ls.DTO.Player.Player
import pt.isel.ls.Services.gameService
import pt.isel.ls.Services.playerService
import pt.isel.ls.Services.sessionsService
import pt.isel.ls.pt.isel.ls.SessionsServer
import pt.isel.ls.WebApi.SessionsApi
import kotlin.test.Test
import pt.isel.ls.DTO.Session.Session

class ServerTest {

    private val server = SessionsServer(SessionsApi(playerService(), gameService(), sessionsService()))

    @Before
    fun startServer() {
        server.start()
    }

    @After
    fun stopServer() {
        server.stop()
    }

    @Test
    fun `invalid content-type`(){
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
    fun `not supported route`() {
        // Arrange
        val request = Request(Method.GET, "/testroutenotsupported")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `method not implemented`() {
        // Arrange
        val request = Request(Method.HEAD, "/players")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.NOT_IMPLEMENTED)
        assert(response.header("Content-Type") == "application/json")
    }

    @Test
    fun `test create player`() {
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
    fun `test create player empty fields`() {
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
    fun `test create player with invalid body`() {
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
    fun `test get player details`() {
        // Arrange
        val request = Request(Method.GET, "/players/1")
        // Act
        val response = server.sessionsHandler(request)
        val playerDetailsJson = response.bodyString()
        val playerDetails = Json.decodeFromString<Player>(playerDetailsJson)
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
        val request = Request(Method.GET, "/players/2")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }
    @Test
    fun `test create game`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","description":"Test","genres":["Test"]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create game empty fields`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"","description":"Test","genres":[""]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.CREATED)
    }
    @Test
    fun `test create game with invalid body`() {
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
    fun `test create game no auth`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","description":"Test","genres":["Test"]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.UNAUTHORIZED)
    }
    @Test
    fun `test get game details`() {
        // Arrange
        val request = Request(Method.GET, "/games/1")
        // Act
        val response = server.sessionsHandler(request)
        val gameDetailsJson = response.bodyString()
        val gameDetails = Json.decodeFromString<Game>(gameDetailsJson)
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
        val request = Request(Method.GET, "/games/2")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `test get game list`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"Test","genres":["TestGenre1"]}""")
        // Act
        val response = server.sessionsHandler(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<List<Game>>(gameListJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(gameList.size == 2)
        assert(gameList[0].name == "TestName1")
        assert(gameList[0].developer == "TestDeveloper1")
        assert(gameList[0].genres == listOf("TestGenre1"))
        assert(gameList[0].gid == 1)
        assert(gameList[1].name == "TestName2")
        assert(gameList[1].developer == "TestDeveloper2")
        assert(gameList[1].genres == listOf("TestGenre2"))
        assert(gameList[1].gid == 2)
    }

    @Test
    fun `test get game list empty fields`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"","genres":[]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
        assert(response.header("Content-Type") == "application/json")
    }
    @Test
    fun `test get game list invalid body`() {
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
    fun `test get game list limit and skip`() {
        // Arrange
        val request = Request(Method.GET, "/games?limit=1&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"developer":"Test","genres":["TestGenre1"]""")
        // Act
        val response = server.sessionsHandler(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<List<Game>>(gameListJson)
        // Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(gameList.size == 1)
        assert(gameList[0].name == "TestName2")
        assert(gameList[0].developer == "TestDeveloper2")
        assert(gameList[0].genres == listOf("TestGenre2"))
        assert(gameList[0].gid == 2)
    }

    @Test
    fun `test create session`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"2021-05-01T00:00:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create session exceeding max capacity supported`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"1000","date":"2021-05-01T00:00:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create session exceeding incorrect date format`() {
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
    fun `test create session no auth`() {
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
        assert(response.status == Status.UNAUTHORIZED)
    }

    @Test
    fun `test create session with invalid body`() {
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
    fun `test create session empty fields`() {
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
    fun `add player to session`() {
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
    fun `add player to session no auth`() {
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
    fun `add player to session, empty fields`() {
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
    fun `add player to session, invalid body`() {
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
    fun `get session details`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1")
            .header("Content-Type", "application/json")
        // Act
        val response = server.sessionsHandler(request)
        val sessionBodyString = response.bodyString()
        val session = Json.decodeFromString<Session>(sessionBodyString)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(session.gid == 1)
        assert(session.capacity == 100)
        assert(session.date == "2021-05-01T00:00:00")
        assert(session.ssid == 1)
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
    fun `test get session list`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1/list")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"date":"2021-05-01T00:00:00","state":"open","pid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<List<Session>>(sessionListJson)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(sessionList.size == 1)
        assert(sessionList[0].gid == 1)
        assert(sessionList[0].capacity == 100)
        assert(sessionList[0].date == "2021-05-01T00:00:00")
        assert(sessionList[0].ssid == 1)
    }

    @Test
    fun `test get session list empty fields`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1/list")
            .header("Content-Type", "application/json")
            .body("""{"gid":""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assert(response.status == Status.BAD_REQUEST)
        assert(response.header("Content-Type") == "application/json")
    }
    @Test
    fun `test get session list invalid body`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1/list")
            .header("Content-Type", "application/json")
            .body("")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test get session list limit and skip`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1/list?limit=1&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"gid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<List<Session>>(sessionListJson)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(sessionList.size == 2)
        assert(sessionList[0].gid == 1)
        assert(sessionList[0].capacity == 100)
        assert(sessionList[0].date == "2021-05-01T00:00:00")
        assert(sessionList[0].ssid == 1)
        assert(sessionList[1].gid == 1)
        assert(sessionList[1].capacity == 100)
        assert(sessionList[1].date == "2021-06-01T00:00:00")
        assert(sessionList[1].ssid == 2)
    }



}