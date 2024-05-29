package pt.isel.ls.server

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pt.isel.ls.SessionsServer
import pt.isel.ls.api.SessionsApi
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toGenre
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.data.dto.GameInfoOutputModel
import pt.isel.ls.data.dto.GameSearchResultOutputModel
import pt.isel.ls.data.dto.PlayerInfoOutputModel
import pt.isel.ls.data.dto.PlayerSearchOutputModel
import pt.isel.ls.dto.SessionInfoOutputModel
import pt.isel.ls.dto.SessionSearchResultOutputModel
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.MemManager
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.plus
import kotlin.time.Duration

class ServerTest {

    @Test
    fun `test create player should create player`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"Testemail@test.pt","password":"TestPassword123#"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.CREATED)
    }

    @Test
    fun `test create player credentials taken`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"Testemail@test.pt","password":"TestPassword123#"}""")
        // Act
        server.sessionsHandler(request)
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create player, invalid email`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","email":"Test.com"},"password":"TestPassword123#"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create player, player with email already exists`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","email":"testemail@test.pt","password":"TestPassword123#"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create player empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"","email":"","password":""}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
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
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test get player details should give player details`() {
        // Arrange
        val request = Request(Method.GET, "/players/2")
        // Act
        val response = server.sessionsHandler(request)
        val playerDetailsJson = response.bodyString()
        val playerDetails = Json.decodeFromString<PlayerInfoOutputModel>(playerDetailsJson)
        // Assert
        assertEquals(response.status, Status.OK)
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(playerDetails.name, "TestName")
        assertEquals(playerDetails.email, "testemail@test.pt")
        assertEquals(playerDetails.pid, 2u)
    }

    @Test
    fun `test get player list invalid name`() {
        // Arrange
        val request = Request(Method.GET, "/players?name=Te")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test get player list partial name should give 2 results`() {
        // Arrange
        val request = Request(Method.GET, "/players?name=test")
        // Act
        val response = server.sessionsHandler(request)
        val playerListJson = response.bodyString()
        val playerList = Json.decodeFromString<PlayerSearchOutputModel>(playerListJson)
        // Assert
        assertEquals(response.status, Status.OK)
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(2, playerList.players.size)
        assertEquals(2, playerList.total)
        assertEquals(2u, playerList.players[0].pid)
        assertEquals("TestName", playerList.players[0].name)
        assertEquals("testemail@test.pt", playerList.players[0].email)
        assertEquals(3u, playerList.players[1].pid)
        assertEquals("TestName2", playerList.players[1].name)
        assertEquals("testemail2@test.pt", playerList.players[1].email)
    }

    @Test
    fun `test get player list empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/players?name=&limit=&skip=")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }


    @Test
    fun `test get player details should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/players/4")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `test create game should create game`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"name":"Test3","developer":"Test","genres":["RPG"]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(Status.CREATED, response.status)
    }

    @Test
    fun `test create game empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"name":"","developer":"Test","genres":[""]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test create game with invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"name":"Test"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test create game, game with name already exists`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"name":"TestName","developer":"Test","genres":["RPG"]}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(Status.BAD_REQUEST, response.status)
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
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test get game details should return game details`() {
        // Arrange
        val request = Request(Method.GET, "/games/2")
        // Act
        val response = server.sessionsHandler(request)
        val gameDetailsJson = response.bodyString()
        val gameDetails = Json.decodeFromString<GameInfoOutputModel>(gameDetailsJson)
        // Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals("TestName123", gameDetails.name)
        assertEquals("TestDeveloper", gameDetails.developer)
        assertEquals(listOf("RPG", "Adventure"), gameDetails.genres)
        assertEquals(2u, gameDetails.gid)
    }

    @Test
    fun `test get game details should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/games/4")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `test get game list should return game list`() {
        // Arrange
        val request = Request(Method.GET, "/games?developer=TestDeveloper&genres=RPG")
        // Act
        val response = server.sessionsHandler(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<GameSearchResultOutputModel>(gameListJson)
        // Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(2, gameList.games.size)
        assertEquals("TestName", gameList.games[0].name)
        assertEquals("TestDeveloper", gameList.games[0].developer)
        assertEquals(listOf("RPG"), gameList.games[0].genres)
        assertEquals(1u, gameList.games[0].gid)
        assertEquals("TestName123", gameList.games[1].name)
        assertEquals("TestDeveloper", gameList.games[1].developer)
        assertEquals(listOf("RPG", "Adventure"), gameList.games[1].genres)
        assertEquals(2u, gameList.games[1].gid)
        assertEquals(2, gameList.total)
    }

    @Test
    fun `test get game list partial name search`() {
        // Arrange
        val request = Request(Method.GET, "/games?name=test")
        // Act
        val response = server.sessionsHandler(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<GameSearchResultOutputModel>(gameListJson)
        // Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(2, gameList.games.size)
        assertEquals("TestName", gameList.games[0].name)
        assertEquals("TestDeveloper", gameList.games[0].developer)
        assertEquals(listOf("RPG"), gameList.games[0].genres)
        assertEquals(1u, gameList.games[0].gid)
        assertEquals("TestName123", gameList.games[1].name)
        assertEquals("TestDeveloper", gameList.games[1].developer)
        assertEquals(listOf("RPG", "Adventure"), gameList.games[1].genres)
        assertEquals(2u, gameList.games[1].gid)
        assertEquals(2, gameList.total)
    }

    @Test
    fun `test get game list empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/games?developer=&genres=")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
    }
    @Test
    fun `test get game list limit and skip should return game list`() {
        // Arrange
        val request = Request(Method.GET, "/games?limit=1&skip=1&developer=TestDeveloper&genres=RPG")
        // Act
        val response = server.sessionsHandler(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<GameSearchResultOutputModel>(gameListJson)
        // Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(1, gameList.games.size)
        assertEquals("TestName123", gameList.games[0].name)
        assertEquals("TestDeveloper", gameList.games[0].developer)
        assertEquals(listOf("RPG", "Adventure"), gameList.games[0].genres)
        assertEquals(2u, gameList.games[0].gid)
        assertEquals(2, gameList.total)
    }

    @Test
    fun `test create session should create session`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"100","date":"${newDate()}"}""")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.CREATED)
    }

    @Test
    fun `test create session exceeding max capacity supported should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"1000","date":"${newDate()}"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create session exceeding incorrect date format should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"1000","date":"0:2340:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"${newDate()}"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.UNAUTHORIZED)
    }

    @Test
    fun `test create session game not found`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":10,"capacity":"100","date":"${newDate()}"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `test create session with invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"description":"Test"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create session empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"","date":""}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session should add player to session`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/2/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `add player to session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .body("""{"pid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.UNAUTHORIZED)
    }

    @Test
    fun `add player to session, session not found`() {
        // Arrange
        val request = Request(Method.POST, "/sessions/10/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":1}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `add player to session, player not found`() {
        // Arrange
        val request = Request(Method.POST, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":10}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `add player to session, empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":""}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `remove player from session should remove player from session`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/2")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `remove player from session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/2")
            .header("Content-Type", "application/json")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.UNAUTHORIZED)
    }

    @Test
    fun `remove player from session player not found`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/10")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `remove player from session session not found`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/10/players/2")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `remove player from session player not in session`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `remove player from session player not in session should give not found`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `update session should update session`() {
        // Arrange
        val request = Request(Method.PATCH, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"100","date":"${newDate()}"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.status, Status.NO_CONTENT)
    }

    @Test
    fun `update session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.PATCH, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"capacity":"200","date":"${newDate()}"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.UNAUTHORIZED)
    }

    @Test
    fun `update session session not found`() {
        // Arrange
        val request = Request(Method.PATCH, "/sessions/10")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"100","date":"${newDate()}"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `update session empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.PATCH, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"","date":""}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `update session invalid capacity should give bad request`() {
        // Arrange
        val request = Request(Method.PATCH, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"200","date":"${newDate()}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `update session invalid date should give bad request`() {
        // Arrange
        val request = Request(Method.PATCH, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"100","date":"2020-05-01T00:00:00"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `update session, capacity less than current players size`() {
        // Arrange
        val request = Request(Method.PATCH, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"1","date":"${newDate()}"}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `delete session, no auth`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1")
            .header("Content-Type", "application/json")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.UNAUTHORIZED)
    }

    @Test
    fun `delete session, not found`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/10")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `delete session, should delete session`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
        // Act
        val response = server.sessionsHandler(request)
        // Assert
        assertEquals(response.status, Status.OK)
    }

    @Test
    fun `add player to session, invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":1""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session player already in session`() {
        // Arrange
        val request = Request(Method.POST, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":2}""")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.status, Status.BAD_REQUEST)
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
        assertEquals(response.status, Status.OK)
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(session.gameSession.gid, 2u)
        assertEquals(session.capacity, 100u)
        assertEquals(session.date, testDate1.toString())
        assertEquals(session.sid, 1u)
    }

    @Test
    fun `get session details not found`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/10")
            .header("Content-Type", "application/json")
        // Act
        val response = server.sessionsHandler(request)
        //  Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `test get session list should return sessions`() {
        // Arrange
        val request = Request(Method.GET, "/sessions?state=open&gid=2")
        // Act
        val response = server.sessionsHandler(request)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchResultOutputModel>(sessionListJson)
        //  Assert
        assertEquals(response.status, Status.OK)
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(sessionList.sessions.size, 2)
        assertEquals(sessionList.sessions[0].gameSession.gid, 2u)
        assertEquals(sessionList.sessions[0].capacity, 100u)
        assertEquals(sessionList.sessions[0].date, testDate1.toString())
        assertEquals(sessionList.sessions[0].sid, 1u)
        assertEquals(sessionList.sessions[1].gameSession.gid, 2u)
        assertEquals(sessionList.sessions[1].capacity, 100u)
        assertEquals(sessionList.sessions[1].date, testDate2.toString())
        assertEquals(sessionList.sessions[1].sid, 2u)
        assertEquals(sessionList.total, 2)
    }

    @Test
    fun `test get session list limit and skip should give session list`() {
        // Arrange
        val request = Request(Method.GET, "/sessions?limit=1&skip=1&gid=2")
        // Act
        val response = server.sessionsHandler(request)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchResultOutputModel>(sessionListJson)
        //  Assert
        assertEquals(response.status, Status.OK)
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(sessionList.sessions.size, 1)
        assertEquals(sessionList.sessions[0].gameSession.gid, 2u)
        assertEquals(sessionList.sessions[0].capacity, 100u)
        assertEquals(sessionList.sessions[0].date, testDate2.toString())
        assertEquals(sessionList.sessions[0].sid, 2u)
        assertEquals(sessionList.total, 2)
    }

    @BeforeEach
    fun start() {
        storage.close()
        api = SessionsApi(
            PlayerService(storage),
            GameService(storage),
            SessionsService(storage),
        )
        server = SessionsServer(api)
        setup()
        server.start()
    }

    @AfterEach
    fun stop() {
        server.stop()
    }

    companion object {

        private var storage = MemManager()

        private var api = SessionsApi(
            PlayerService(storage),
            GameService(storage),
            SessionsService(storage),
        )

        private var server = SessionsServer(api)

        private val testDate1 = currentLocalTime() + Duration.parse("PT1H")
        private val testDate2 = currentLocalTime() + Duration.parse("PT2H")

        fun newDate() = currentLocalTime() + Duration.parse("PT1H")

        fun setup() {
            val mockGame = Game(1u, "TestName".toName(), "TestDeveloper".toName(), setOf("RPG".toGenre()))
            val mockGame2 = Game(2u, "TestName123".toName(), "TestDeveloper".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))

            val mockSession = Session(1u, 100u, testDate1, mockGame2, setOf())
            val mockSession2 = Session(2u, 100u, testDate2, mockGame2, setOf())

            val mockPlayer = Player(2u, "TestName".toName(), "testemail@test.pt".toEmail(), PasswordHash("TestPassword123#"))
            val mockPlayer2 = Player(3u, "TestName2".toName(), "testemail2@test.pt".toEmail(), PasswordHash("TestPassword123#"))

            storage.game.create(mockGame)
            storage.game.create(mockGame2)

            storage.session.create(mockSession)
            storage.session.create(mockSession2)

            storage.player.create(mockPlayer)
            storage.player.create(mockPlayer2)

            storage.session.addPlayer(mockSession.id, mockPlayer.id)
            storage.session.addPlayer(mockSession.id, mockPlayer2.id)
        }
    }
}