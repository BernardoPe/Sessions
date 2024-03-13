package pt.isel.ls.WebApi

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.DTO.Game.Game
import pt.isel.ls.Services.gameService
import pt.isel.ls.Services.playerService
import pt.isel.ls.Services.sessionsService
import kotlin.test.Test

class GameEndpointsTest {

    private val api = SessionsApi(playerService(), gameService(), sessionsService())
    @Test
    fun `test create game`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","description":"Test","genres":["Test"]}""")
        // Act
        val response = api.processRequest(request, api.createGame)
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
        val response = api.processRequest(request, api.createGame)
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
        val response = api.processRequest(request, api.createGame)
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
        val response = api.processRequest(request, api.createGame)
        // Assert
        assert(response.status == Status.UNAUTHORIZED)
    }
    @Test
    fun `test get game details`() {
        // Arrange
        val request = Request(Method.GET, "/games/1")
        // Act
        val response = api.processRequest(request, api.getGameDetails)
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
        val response = api.processRequest(request, api.getGameDetails)
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
        val response = api.processRequest(request, api.getGameList)
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
        val response = api.processRequest(request, api.getGameList)
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
        val response = api.processRequest(request, api.getGameList)
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
        val response = api.processRequest(request, api.getGameList)
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

}