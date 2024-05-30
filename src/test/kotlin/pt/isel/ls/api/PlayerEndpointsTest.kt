package pt.isel.ls.api

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.cookies
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeEach
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.PasswordHash
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
            .body("""{"name":"Test","email":"Testemail@test.pt","password":"TestPassword#123"}""")
        // Act
        val response = api.createPlayer(request)
        // Assert
        assertEquals(Status.CREATED, response.status)
        assertEquals("application/json", response.header("Content-Type"))
    }

    @Test
    fun `test create player credentials taken`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"Testemail@test.pt","password":"TestPassword#123"}""")
        // Act
        api.createPlayer(request)
        val response = api.createPlayer(request)
        // Assert
        assertEquals(Status.BAD_REQUEST, response.status)
        assertEquals("application/json", response.header("Content-Type"))
    }

    @Test
    fun `test create player, invalid email`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"TestName","email":"Test.com","password":"TestPassword"}""")
        // Act
        val response = api.createPlayer(request)
        // Assert
        assertEquals(Status.BAD_REQUEST, response.status)
        assertEquals("application/json", response.header("Content-Type"))
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
        assertEquals(Status.BAD_REQUEST, response.status)
        assertEquals("application/json", response.header("Content-Type"))
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
        assertEquals(Status.BAD_REQUEST, response.status)
        assertEquals("application/json", response.header("Content-Type"))
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals("TestName", playerDetails.name)
        assertEquals("TestEmail@test.pt", playerDetails.email)
        assertEquals(2u, playerDetails.pid)
    }

    @Test
    fun `test get player details should give not found`() {
        // Arrange
        val request = Request(Method.GET, "/players/4")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/players/{pid}"))
        // Act
        val response = api.getPlayerDetails(routedRequest)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `test get player list should give player list`() {
        // Arrange
        val request = Request(Method.GET, "/players?name=test&limit=1&skip=0")
        // Act
        val response = api.getPlayerList(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.OK, response.status)

        val playerListJson = response.bodyString()
        val playerSearch = Json.decodeFromString<PlayerSearchOutputModel>(playerListJson)

        val players = playerSearch.players

        assertEquals(1, players.size)
        assertEquals("TestName", players[0].name)
        assertEquals("TestEmail@test.pt", players[0].email)

        assertEquals(2, playerSearch.total)
    }

    @Test
    fun `test authPlayer no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/auth")
            .header("Content-Type", "application/json")
        // Act
        val response = api.authPlayer(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test authPlayer should give ok`() {
        // Arrange
        val request = Request(Method.POST, "/auth")
            .header("Content-Type", "application/json")
            .cookie("Authorization", "00000000-0000-0000-0000-000000000000")
        // Act
        val response = api.authPlayer(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.OK, response.status)
    }

    @Test
    fun `test loginPlayer should give ok`() {
        // Arrange
        val request = Request(Method.POST, "/login")
            .header("Content-Type", "application/json")
            .body("""{"name":"John Doe","password":"TestPassword#123"}""")
        // Act
        val response = api.loginPlayer(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.OK, response.status)
    }

    @Test
    fun `test loginPlayer incorrect password should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/login")
            .header("Content-Type", "application/json")
            .body("""{"name":"John Doe", "password":"TestPassword"}""")
        // Act
        val response = api.loginPlayer(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
    }


    @Test
    fun `test playerLogout should give unauthorized`() {
        // Arrange
        val request = Request(Method.GET, "/logout")
        // Act
        val response = api.playerLogout(request)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test player logout should give ok`() {
        // Arrange
        val newPlayerRequest = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"testmail@gmail.com","password":"TestPassword#123"}""")
        val newPlayerResponse = api.createPlayer(newPlayerRequest)

        val request = Request(Method.GET, "/logout")
            .header("Content-Type", "application/json")
            .cookie("Authorization", newPlayerResponse.cookies().first().value)
        // Act
        val response = api.playerLogout(request)
        // Assert
        assertEquals(Status.OK, response.status)
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
            storage.player.create(Player(0u, "TestName".toName(), "TestEmail@test.pt".toEmail(), PasswordHash("TestPassword")))
            storage.player.create(Player(0u, "TestName2".toName(), "TestEmail2@test.pt".toEmail(),  PasswordHash("TestPassword")))
        }
    }
}
