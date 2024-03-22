package pt.isel.ls.api

import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeAll
import pt.isel.ls.data.domain.*
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.dto.SessionInfoOutputModel
import pt.isel.ls.dto.SessionSearchResultOutputModel
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import pt.isel.ls.utils.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals


class SessionEndpointsTest {

    @Test
    fun `test create session should create session`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"100","date":"2030-05-01T00:00:00"}""")
        // Act
        val response = api.createSession(request)
        // Assert
        assertEquals(response.status,Status.CREATED)
    }

    @Test
    fun `test create session exceeding max capacity supported should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"1000","date":"2030-05-01T00:00:00"}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.BAD_REQUEST)
    }

    @Test
    fun `test create session exceeding incorrect date format should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"1000","date":"0:2340:00"}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.BAD_REQUEST)
    }

    @Test
    fun `test create session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"2030-05-01T00:00:00"}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.UNAUTHORIZED)
    }

    @Test
    fun `test create session game not found`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":10,"capacity":"100","date":"2030-05-01T00:00:00"}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.NOT_FOUND)
    }

    @Test
    fun `test create session with invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"description":"Test"}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.BAD_REQUEST)
    }

    @Test
    fun `test create session empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"","date":""}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session should add player to session`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/2/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":1}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players"))
        // Act
        val response = api.addPlayerToSession(routedRequest)
        //  Assert
        assertEquals(response.status,Status.OK)
    }

    @Test
    fun `add player to session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .body("""{"pid":1}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players"))
        // Act
        val response = api.addPlayerToSession(routedRequest)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.UNAUTHORIZED)
    }
    @Test
    fun `add player to session, session not found`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/10/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":1}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players"))
        // Act
        val response = api.addPlayerToSession(routedRequest)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.NOT_FOUND)
    }

    @Test
    fun `add player to session, player not found`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":10}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.addPlayerToSession(routedRequest)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.NOT_FOUND)
    }

    @Test
    fun `add player to session, empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":""}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players"))
        // Act
        val response = api.addPlayerToSession(routedRequest)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session, invalid body should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":1""")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.addPlayerToSession(routedRequest)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.BAD_REQUEST)
    }

    @Test
    fun `add player to session player already in session`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1/players")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"pid":2}""")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players"))
        // Act
        val response = api.addPlayerToSession(routedRequest)
        //  Assert
        assertEquals(response.status,Status.CONFLICT)
    }

    @Test
    fun `get session details should give session details`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/1")
            .header("Content-Type", "application/json")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.getSessionById(routedRequest)
        val sessionBodyString = response.bodyString()
        val session = Json.decodeFromString<SessionInfoOutputModel>(sessionBodyString)
        //  Assert
        assertEquals(response.status,Status.OK)
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(session.gameSession.gid,2u)
        assertEquals(session.capacity,100u)
        assertEquals(session.date,"2030-05-01T00:00:00".toLocalDateTime().toString())
        assertEquals(session.sid,1u)
    }

    @Test
    fun `get session details not found`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/10")
            .header("Content-Type", "application/json")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.getSessionById(routedRequest)
        //  Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.NOT_FOUND)
    }

    @Test
    fun `test get session list should return sessions`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/2/list?state=open")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{gid}/list"))
        // Act
        val response = api.getSessionList(routedRequest)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchResultOutputModel>(sessionListJson)
        //  Assert
        assertEquals(response.status,Status.OK)
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(sessionList.size,2)
        assertEquals(sessionList[0].gameSession.gid,2u)
        assertEquals(sessionList[0].capacity,100u)
        assertEquals(sessionList[0].date,"2030-05-01T00:00:00".toLocalDateTime().toString())
        assertEquals(sessionList[0].sid,1u)
        assertEquals(sessionList[1].gameSession.gid,2u)
        assertEquals(sessionList[1].capacity,100u)
        assertEquals(sessionList[1].date,"2030-06-01T00:00:00".toLocalDateTime().toString())
        assertEquals(sessionList[1].sid,2u)
    }
    @Test
    fun `test get session list invalid params should give bad request`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/asd/list")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{gid}/list"))
        // Act
        val response = api.getSessionList(routedRequest)
        // Assert
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(response.status,Status.BAD_REQUEST)
    }
    @Test
    fun `test get session list limit and skip should give session list`() {
        // Arrange
        val request = Request(Method.GET, "/sessions/2/list?limit=1&skip=1")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{gid}/list"))
        // Act
        val response = api.getSessionList(routedRequest)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchResultOutputModel>(sessionListJson)
        //  Assert
        assertEquals(response.status,Status.OK)
        assertEquals(response.header("Content-Type"),"application/json")
        assertEquals(sessionList.size,1)
        assertEquals(sessionList[0].gameSession.gid,2u)
        assertEquals(sessionList[0].capacity,100u)
        assertEquals(sessionList[0].date,"2030-06-01T00:00:00".toLocalDateTime().toString())
        assertEquals(sessionList[0].sid,2u)
    }


    companion object {

        private val storage = SessionsDataManager(SessionsDataMemGame(), SessionsDataMemPlayer(), SessionsDataMemSession())

        private val api = SessionsApi(PlayerService(storage), GameService(storage), SessionsService(storage))

        @JvmStatic
        @BeforeAll
        fun setup() {
            val mockGame = Game(2u, "TestName".toName(), "TestDeveloper".toName(), setOf("RPG".toGenre()))
            storage.game.create("TestName123".toName(), "TestDeveloper123".toName(), setOf("RPG".toGenre()))
            storage.game.create("TestName".toName(), "TestDeveloper".toName(), setOf("RPG".toGenre()))
            storage.session.create(Session(1u,100u, "2030-05-01T00:00:00".toLocalDateTime(), mockGame, setOf()))
            storage.session.create(Session(2u,100u, "2030-06-01T00:00:00".toLocalDateTime(), mockGame, setOf()))
            storage.player.create("TestName".toName(), "testemail@test.pt".toEmail())
            val req = Request(Method.PUT, "/sessions/1")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
                .body("""{"pid":2}""")
            val routedReq = RoutedRequest(req, UriTemplate.from("/sessions/{sid}"))
            api.addPlayerToSession(routedReq)
        }

    }
}