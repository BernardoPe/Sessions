package pt.isel.ls.api

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeAll
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.dto.GameInfoOutputModel
import pt.isel.ls.dto.GameSearchOutputModel
import pt.isel.ls.storage.SessionsDataPlayer
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import kotlin.test.Test

/*
class GameEndpointsTest {

    @Test
    fun `test create game should create game`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test3","developer":"Test","genres":["Test"]}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_GAME)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create game empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"","developer":"Test","genres":[""]}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_GAME)
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
        val response = api.processRequest(request, Operation.CREATE_GAME)
        // Assert
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test create game, game with name already exists`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","developer":"Test","genres":["Test"]}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_GAME)
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
        val response = api.processRequest(request, Operation.CREATE_GAME)
        // Assert
        assert(response.status == Status.UNAUTHORIZED)
    }
    @Test
    fun `test get game details should return game details`() {
        // Arrange
        val request = Request(Method.GET, "/games/1")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/{gid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.GET_GAME_DETAILS)
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
    fun `test get game details should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/games/3")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/{gid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.GET_GAME_DETAILS)
        // Assert
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `test get game list should return game list`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"Test","genres":["TestGenre1"]}""")
        // Act
        val response = api.processRequest(request, Operation.GET_GAME_LIST)
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
    fun `test get game list empty fields should return empty array`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"","genres":[]}""")
        // Act
        val response = api.processRequest(request, Operation.GET_GAME_LIST)
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
        val response = api.processRequest(request, Operation.GET_GAME_LIST)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }



    @Test
    fun `test get game list limit and skip should return game list`() {
        // Arrange
        val request = Request(Method.GET, "/games?limit=1&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"developer":"Test","genres":["TestGenre1"]""")
        // Act
        val response = api.processRequest(request, Operation.GET_GAME_LIST)
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

    companion object {

        private val gameStorage = SessionsDataMemGame()

        private val api = SessionsApi(playerService(SessionsDataMemPlayer()), gameService(gameStorage), sessionsService(SessionsDataMemSession()))
        @JvmStatic
        @BeforeAll
        fun setup(): Unit {
            val mockGame1 = Game(1, "TestName", "TestDeveloper", setOf("TestGenre"))
            val mockGame2 = Game(2, "TestName2", "TestDeveloper2", setOf("TestGenre"))
            gameStorage.create(mockGame1)
            gameStorage.create(mockGame2)
        }

    }

}*/