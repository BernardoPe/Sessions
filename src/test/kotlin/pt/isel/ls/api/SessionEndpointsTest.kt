package pt.isel.ls.api

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeAll
import pt.isel.ls.domain.game.Game
import pt.isel.ls.domain.player.Player
import pt.isel.ls.domain.session.Session
import pt.isel.ls.dto.SessionInfoOutputModel
import pt.isel.ls.dto.SessionSearchOutputModel
import pt.isel.ls.services.gameService
import pt.isel.ls.services.playerService
import pt.isel.ls.services.sessionsService
import pt.isel.ls.storage.SessionsDataPlayer
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import kotlin.test.Test

/*
class SessionEndpointsTest {

    @Test
    fun `test create session should create player`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"2021-05-01 00:00:00"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_SESSION)
        // Assert
        assert(response.status == Status.CREATED)
    }

    @Test
    fun `test create session exceeding max capacity supported should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"1000","date":"2021-05-01T00:00:00"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create session exceeding incorrect date format should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"1000","date":"0:2340:00"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"2021-05-01 00:00:00"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.UNAUTHORIZED)
    }

    @Test
    fun `test create session game not found`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":10,"capacity":"100","date":"2021-05-01 00:00:00"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `test create session with invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"description":"Test"}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create session empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"","date":""}""")
        // Act
        val response = api.processRequest(request, Operation.CREATE_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session should add player to session`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":1}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.ADD_PLAYER_TO_SESSION)
        //  Assert
        assert(response.status == Status.OK)
    }

    @Test
    fun `add player to session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":1}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.ADD_PLAYER_TO_SESSION)
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
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.ADD_PLAYER_TO_SESSION)
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
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.ADD_PLAYER_TO_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `add player to session, empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":""}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.ADD_PLAYER_TO_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session, invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":1""")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.ADD_PLAYER_TO_SESSION)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session player already in session`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"pid":2}""")
        // Act
        val response = api.processRequest(request, Operation.ADD_PLAYER_TO_SESSION)
        //  Assert
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `get session details should give session details`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1")
            .header("Content-Type", "application/json")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.GET_SESSION_DETAILS)
        val sessionBodyString = response.bodyString()
        val session = Json.decodeFromString<SessionInfoOutputModel>(sessionBodyString)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(session.gameSession.gid == 1)
        assert(session.capacity == 100)
        assert(session.date == "2021-05-01 00:00:00")
        assert(session.sid == 1)
    }

    @Test
    fun `get session details not found`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/10")
            .header("Content-Type", "application/json")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.processRequest(routedRequest, Operation.GET_SESSION_DETAILS)
        //  Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.NOT_FOUND)
    }

    @Test
    fun `test get session list should return sessions`() {
        // Arrange
        val request = Request(Method.GET, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"date":"2021-05-0 T00:00:00","state":"open","pid":1}""")
        // Act
        val response = api.processRequest(request, Operation.GET_SESSION_LIST)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchOutputModel>(sessionListJson)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(sessionList.sessions.size == 1)
        assert(sessionList.sessions[0].gameSession.gid == 1)
        assert(sessionList.sessions[0].capacity == 100)
        assert(sessionList.sessions[0].date == "2021-05-01T00:00:00")
        assert(sessionList.sessions[0].sid == 1)
    }

    @Test
    fun `test get session list empty fields should return empty array`() {
        // Arrange
        val request = Request(Method.GET, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":""")
        // Act
        val response = api.processRequest(request, Operation.GET_SESSION_LIST)
        //  Assert
        assert(response.status == Status.BAD_REQUEST)
        assert(response.header("Content-Type") == "application/json")
        assert(response.bodyString() == """{"sessions":[]}""")
    }
    @Test
    fun `test get session list invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/sessions")
            .header("Content-Type", "application/json")
            .body("")
        // Act
        val response = api.processRequest(request, Operation.GET_SESSION_LIST)
        // Assert
        assert(response.header("Content-Type") == "application/json")
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test get session list limit and skip should give session list`() {
        // Arrange
        val request = Request(Method.GET, "/sessions?limit=2&skip=1")
            .header("Content-Type", "application/json")
            .body("""{"gid":1}""")
        // Act
        val response = api.processRequest(request, Operation.GET_SESSION_LIST)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchOutputModel>(sessionListJson)
        //  Assert
        assert(response.status == Status.OK)
        assert(response.header("Content-Type") == "application/json")
        assert(sessionList.sessions.size == 2)
        assert(sessionList.sessions[0].gameSession.gid == 1)
        assert(sessionList.sessions[0].capacity == 100)
        assert(sessionList.sessions[0].date == "2021-05-01T00:00:00")
        assert(sessionList.sessions[0].sid == 1)
        assert(sessionList.sessions[1].gameSession.gid == 1)
        assert(sessionList.sessions[1].capacity == 100)
        assert(sessionList.sessions[1].date == "2021-06-01T00:00:00")
        assert(sessionList.sessions[1].sid == 2)
    }


    companion object {

        private val sessionStorage = SessionsDataMemSession()

        private val api = SessionsApi(
                playerService(SessionsDataMemPlayer()),
                gameService(SessionsDataMemGame()),
                sessionsService(sessionStorage)
            )

        @JvmStatic
        @BeforeAll
        fun `setup`(): Unit {

            val mockGame1 = Game(1, "TestName", "TestDeveloper", setOf("TestGenre"))
            val mockSession = Session(1, 100, "2021-05-01T00:00:00", mockGame1, setOf())
            val mockSession2 = Session(2, 100, "2021-06-01T00:00:00", mockGame1, setOf())

            sessionStorage.create(mockSession)
            sessionStorage.create(mockSession2)

        }

    }
}*/