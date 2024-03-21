import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeAll
import pt.isel.ls.api.SessionsApi
import pt.isel.ls.data.domain.toEmail
import pt.isel.ls.data.domain.toGenre
import pt.isel.ls.data.domain.toName
import pt.isel.ls.dto.GameInfoOutputModel
import pt.isel.ls.dto.GameSearchOutputModel
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import kotlin.test.Test
import kotlin.test.assertEquals

class GameEndpointsTest {

    @Test
    fun `test create game should create game`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"name":"Test3","developer":"Test","genres":["RPG"]}""")
        // Act
        val response = api.createGame(request)
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
        val response = api.createGame(request)
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
        val response = api.createGame(request)
        // Assert
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test create game, game with name already exists`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"name":"Test","developer":"Test","genres":["RPG"]}""")
        // Act
        val response = api.createGame(request)
        // Assert
        assertEquals(Status.CREATED, response.status)
    }

    @Test
    fun `test create game no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","developer":"Test","genres":["Test"]}""")
        // Act
        val response = api.createGame(request)
        // Assert
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test get game details should return game details`() {
        // Arrange
        val request = Request(Method.GET, "/games/1")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/{gid}"))
        // Act
        val response = api.getGameById(routedRequest)
        val gameDetailsJson = response.bodyString()
        val gameDetails = Json.decodeFromString<GameInfoOutputModel>(gameDetailsJson)
        // Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals("TestName2", gameDetails.name)
        assertEquals("TestDeveloper", gameDetails.developer)
        assertEquals(listOf("RPG"), gameDetails.genres)
        assertEquals(1u, gameDetails.gid)
    }

    @Test
    fun `test get game details should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/games/3")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/games/{gid}"))
        // Act
        val response = api.getGameById(routedRequest)
        // Assert
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `test get game list should return game list`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"TestDeveloper","genres":["RPG"]}""")
        // Act
        val response = api.getGameList(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<GameSearchOutputModel>(gameListJson)
        // Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(2, gameList.games.size)
        assertEquals("TestName", gameList.games[0].name)
        assertEquals("TestDeveloper", gameList.games[0].developer)
        assertEquals(listOf("RPG", "Adventure"), gameList.games[0].genres)
        assertEquals(0u, gameList.games[0].gid)
        assertEquals("TestName2", gameList.games[1].name)
        assertEquals("TestDeveloper", gameList.games[1].developer)
        assertEquals(listOf("RPG"), gameList.games[1].genres)
        assertEquals(1u, gameList.games[1].gid)
    }

    @Test
    fun `test get game list empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("""{"developer":"","genres":[]}""")
        // Act
        val response = api.getGameList(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test get game list invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/games")
            .header("Content-Type", "application/json")
            .body("")
        // Act
        val response = api.getGameList(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test get game list limit and skip should return game list`() {
        // Arrange
        val request = Request(Method.GET, "/games?limit=1&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"developer":"TestDeveloper","genres":["RPG"]}""")
        // Act
        val response = api.getGameList(request)
        val gameListJson = response.bodyString()
        val gameList = Json.decodeFromString<GameSearchOutputModel>(gameListJson)
        // Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(1, gameList.games.size)
        assertEquals("TestName2", gameList.games[0].name)
        assertEquals("TestDeveloper", gameList.games[0].developer)
        assertEquals(listOf("RPG"), gameList.games[0].genres)
        assertEquals(1u, gameList.games[0].gid)
    }

    companion object {

        private val storage = SessionsDataManager(SessionsDataMemGame(), SessionsDataMemPlayer(), SessionsDataMemSession())

        private val api = SessionsApi(PlayerService(storage), GameService(storage), SessionsService(storage))

        @JvmStatic
        @BeforeAll
        fun setup(): Unit {
            storage.game.create( "TestName".toName(), "TestDeveloper".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
            storage.game.create( "TestName2".toName(), "TestDeveloper".toName(), setOf("RPG".toGenre()))
            storage.player.create("TestName".toName(), "TestEmail@test.pt".toEmail())
            storage.player.create("TestName2".toName(), "TestEmail2@test.pt".toEmail())
        }

    }

}
