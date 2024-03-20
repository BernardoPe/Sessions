package pt.isel.ls.api

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeAll
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.dto.PlayerInfoOutputModel
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import kotlin.test.Test

/**

class PlayerEndpointsTest {

    @Test
    fun `test create player should create player`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"Test"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_PLAYER)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create player, invalid email`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","email":"Test.com"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_PLAYER)
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
        val response = api.processRequest(request, Operation.CREATE_PLAYER)
        // Assert
        assert(response.status == Status.CONFLICT)
    }

    @Test
    fun `test create player empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"","email":""}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_PLAYER)
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
        val response = api.processRequest(request, Operation.CREATE_PLAYER)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test get player details should give player details`() {
        // Arrange
        val request = Request(Method.GET, "/players/1")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/players/{pid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.GET_PLAYER_DETAILS)
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
    fun `test get player details should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/players/3")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/players/{pid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.GET_PLAYER_DETAILS)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

    companion object {

        private val playerStorage = SessionsDataMemPlayer()
        private val api = SessionsApi(playerService(playerStorage), gameService(SessionsDataMemGame()), sessionsService(SessionsDataMemSession()))

        @JvmStatic
        @BeforeAll
        fun setup() {
            val mockPlayer = Player(1, "TestName", "TestEmail")
            val mockPlayer2 = Player(2, "TestName2", "TestEmail@gmail.com")
            playerStorage.create(mockPlayer)
            playerStorage.create(mockPlayer2)
        }
    }

}

*/