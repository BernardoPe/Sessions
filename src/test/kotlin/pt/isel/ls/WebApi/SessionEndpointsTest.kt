package pt.isel.ls.WebApi

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import pt.isel.ls.DTO.Session.Session
import pt.isel.ls.Services.gameService
import pt.isel.ls.Services.playerService
import pt.isel.ls.Services.sessionsService
import kotlin.test.Test

class SessionEndpointsTest {

    private val api = SessionsApi(playerService(), gameService(), sessionsService())
    @Test
    fun `test create session`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"2021-05-01T00:00:00"}""")
        // Act
        val response = api.processRequest(request, api.createSession)
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
        val response = api.processRequest(request, api.createSession)
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
        val response = api.processRequest(request, api.createSession)
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
        val response = api.processRequest(request, api.createSession)
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
        val response = api.processRequest(request, api.createSession)
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
        val response = api.processRequest(request, api.createSession)
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
        val response = api.processRequest(request, api.createSession)
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
        val response = api.processRequest(request, api.addPlayerToSession)
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
        val response = api.processRequest(request, api.addPlayerToSession)
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
        val response = api.processRequest(request, api.addPlayerToSession)
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
        val response = api.processRequest(request, api.addPlayerToSession)
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
        val response = api.processRequest(request, api.addPlayerToSession)
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
        val response = api.processRequest(request, api.addPlayerToSession)
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
        val response = api.processRequest(request, api.getSessionDetails)
        val sessionBodyString = response.bodyString()
        val session = Json.decodeFromString<Session>(sessionBodyString)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(session.gameSession.gid == 1)
        assert(session.capacity == 100)
        assert(session.date == "2021-05-01T00:00:00")
        assert(session.sid == 1)
    }

    @Test
    fun `get session details not found`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/10")
            .header("Content-Type", "application/json")
        // Act
        val response = api.processRequest(request, api.getSessionDetails)
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
        val response = api.processRequest(request, api.getSessionList)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<List<Session>>(sessionListJson)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(sessionList.size == 1)
        assert(sessionList[0].gameSession.gid == 1)
        assert(sessionList[0].capacity == 100)
        assert(sessionList[0].date == "2021-05-01T00:00:00")
        assert(sessionList[0].sid == 1)
    }

    @Test
    fun `test get session list empty fields`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1/list")
            .header("Content-Type", "application/json")
            .body("""{"gid":""")
        // Act
        val response = api.processRequest(request, api.getSessionList)
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
        val response = api.processRequest(request, api.getSessionList)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test get session list limit and skip`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1/list?limit=2&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"gid":1}""")
        // Act
        val response = api.processRequest(request, api.getSessionList)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<List<Session>>(sessionListJson)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(sessionList.size == 2)
        assert(sessionList[0].gameSession.gid == 1)
        assert(sessionList[0].capacity == 100)
        assert(sessionList[0].date == "2021-05-01T00:00:00")
        assert(sessionList[0].sid == 1)
        assert(sessionList[1].gameSession.gid == 1)
        assert(sessionList[1].capacity == 100)
        assert(sessionList[1].date == "2021-06-01T00:00:00")
        assert(sessionList[1].sid == 2)
    }
}