package pt.isel.ls.api

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeEach
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.dto.PlayerInfoOutputModel
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
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

    @BeforeEach
    fun clear() {
        storage = SessionsDataManager(SessionsDataMemGame(), SessionsDataMemPlayer(), SessionsDataMemSession())
        api = SessionsApi(PlayerService(storage), GameService(storage), SessionsService(storage))
        setup()
    }

    companion object {

        private var storage = SessionsDataManager(SessionsDataMemGame(), SessionsDataMemPlayer(), SessionsDataMemSession())

        private var api = SessionsApi(PlayerService(storage), GameService(storage), SessionsService(storage))

        fun setup() {
            storage.player.create(Player(0u, "TestName".toName(), "TestEmail@test.pt".toEmail(), 0L))
            storage.player.create(Player(0u, "TestName2".toName(), "TestEmail2@test.pt".toEmail(), 0L))
        }
    }
}
