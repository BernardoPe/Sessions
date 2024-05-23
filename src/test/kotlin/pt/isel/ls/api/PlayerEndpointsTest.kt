package pt.isel.ls.api

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeEach
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.dto.PlayerInfoOutputModel
import pt.isel.ls.data.dto.PlayerSearchOutputModel
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.MemManager
import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerEndpointsTest {

    @Test
    fun `test create player should create player`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"Testemail@test.pt"}""")
        // Act
        val response = api.createPlayer(request)
        // Assert
        assertEquals(response.status, Status.CREATED)
    }

    @Test
    fun `test create player name taken`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"Testemail@test.pt"}""")
        // Act
        api.createPlayer(request)
        val response = api.createPlayer(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create player, invalid email`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","email":"Test.com"}""")
        // Act
        val response = api.createPlayer(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create player, player with email already exists`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","email":"TestEmail@test.pt"}""")
        // Act
        val response = api.createPlayer(request)
        // Assert
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test create player empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"","email":""}""")
        // Act
        val response = api.createPlayer(request)
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
        val response = api.createPlayer(request)
        // Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.BAD_REQUEST)
    }

    @Test
    fun `test get player details should give player details`() {
        // Arrange
        val request = Request(Method.GET, "/players/2")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/players/{pid}"))
        // Act
        val response = api.getPlayerDetails(routedRequest)
        val playerDetailsJson = response.bodyString()
        val playerDetails = Json.decodeFromString<PlayerInfoOutputModel>(playerDetailsJson)
        // Assert
        assertEquals(response.status, Status.OK)
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(playerDetails.name, "TestName")
        assertEquals(playerDetails.email, "TestEmail@test.pt")
        assertEquals(playerDetails.pid, 2u)
    }

    @Test
    fun `test get player details should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/players/4")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/players/{pid}"))
        // Act
        val response = api.getPlayerDetails(routedRequest)
        // Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.NOT_FOUND)
    }

    @Test
    fun `test get player list should give player list`() {
        // Arrange
        val request = Request(Method.GET, "/players?name=test&limit=1&skip=0")
        // Act
        val response = api.getPlayerList(request)
        // Assert
        assertEquals(response.header("Content-Type"), "application/json")
        assertEquals(response.status, Status.OK)

        val playerListJson = response.bodyString()
        val playerSearch = Json.decodeFromString<PlayerSearchOutputModel>(playerListJson)

        val players = playerSearch.players

        assertEquals(players.size, 1)
        assertEquals(players[0].name, "TestName")
        assertEquals(players[0].email, "TestEmail@test.pt")

        assertEquals(2, playerSearch.total)

    }

    @BeforeEach
    fun clear() {
        storage.close()
        api = SessionsApi(PlayerService(storage), GameService(storage), SessionsService(storage))
        setup()
    }

    companion object {

        private var storage = MemManager()

        private var api = SessionsApi(PlayerService(storage), GameService(storage), SessionsService(storage))

        fun setup() {
            storage.player.create(Player(0u, "TestName".toName(), "TestEmail@test.pt".toEmail(), 0L))
            storage.player.create(Player(0u, "TestName2".toName(), "TestEmail2@test.pt".toEmail(), 0L))
        }
    }
}
