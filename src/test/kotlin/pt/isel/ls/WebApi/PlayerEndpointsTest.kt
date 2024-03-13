package pt.isel.ls.WebApi

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.DTO.Player.Player
import pt.isel.ls.Services.gameService
import pt.isel.ls.Services.playerService
import pt.isel.ls.Services.sessionsService
import kotlin.test.Test

class PlayerEndpointsTest {

    private val api = SessionsApi(playerService(), gameService(), sessionsService())
    @Test
    fun `test create player`() {
        // Arrange
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":"Test"}""")
        // Act
        val response = api.processRequest(request, api.createPlayer)
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
        val response = api.processRequest(request, api.createPlayer)
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
        val response = api.processRequest(request, api.createPlayer)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test get player details`() {
        // Arrange
        val request = Request(Method.GET, "/players/1")
        // Act
        val response = api.processRequest(request, api.getPlayerDetails)
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
        val response = api.processRequest(request, api.getPlayerDetails)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

}