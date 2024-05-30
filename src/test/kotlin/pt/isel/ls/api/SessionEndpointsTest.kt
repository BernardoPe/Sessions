package pt.isel.ls.api

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.UriTemplate
import org.http4k.routing.RoutedRequest
import org.junit.jupiter.api.BeforeEach
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.dto.SessionInfoOutputModel
import pt.isel.ls.data.dto.SessionSearchResultOutputModel
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toGenre
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.MemManager
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration

class SessionEndpointsTest {

    @Test
    fun `test create session should create session`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"100","date":"${newDate()}"}""")
        // Act
        val response = api.createSession(request)
        // Assert
        assertEquals(Status.CREATED, response.status)
    }

    @Test
    fun `test create session exceeding max capacity supported should give bad request`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":1,"capacity":"1000","date":"${newDate()}"}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `test create session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .body("""{"gid":1,"capacity":"100","date":"${newDate()}"}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `test create session game not found`() {
        // Arrange
        val request = Request(Method.POST, "/sessions")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"gid":10,"capacity":"100","date":"${newDate()}"}""")
        // Act
        val response = api.createSession(request)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
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
        assertEquals(Status.CREATED, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.UNAUTHORIZED, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
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
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `remove player from session should remove player from session`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/2")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players/{pid}"))
        // Act
        val response = api.removePlayerFromSession(routedRequest)
        //  Assert
        assertEquals(Status.NO_CONTENT, response.status)
    }

    @Test
    fun `remove player from session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/2")
            .header("Content-Type", "application/json")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players/{pid}"))
        // Act
        val response = api.removePlayerFromSession(routedRequest)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `remove player from session, session not found`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/10/players/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players/{pid}"))
        // Act
        val response = api.removePlayerFromSession(routedRequest)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `remove player from session, player not found`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/10")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players/{pid}"))
        // Act
        val response = api.removePlayerFromSession(routedRequest)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `remove player from session player not in session`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players/{pid}"))
        // Act
        val response = api.removePlayerFromSession(routedRequest)
        //  Assert
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `remove player from session player not in session should give not found`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1/players/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}/players/{pid}"))
        // Act
        val response = api.removePlayerFromSession(routedRequest)
        //  Assert
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `update session should update session`() {
        // Arrange
        val request = Request(Method.PATCH, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"100","date":"${newDate()}"}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.updateSession(routedRequest)
        //  Assert
        assertEquals(Status.NO_CONTENT, response.status)
    }

    @Test
    fun `update session no auth should give unauthorized`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .body("""{"capacity":"200","date":"${newDate()}"}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.updateSession(routedRequest)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `update session session not found`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/10")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"100","date":"${newDate()}"}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.updateSession(routedRequest)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `update session empty fields should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"","date":""}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.updateSession(routedRequest)
        //  Assert
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `update session invalid capacity should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"200","date":"${newDate()}"}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.updateSession(routedRequest)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `update session invalid date should give bad request`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"100","date":"2020-05-01T00:00:00"}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.updateSession(routedRequest)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `update session, capacity less than current players size`() {
        // Arrange
        val request = Request(Method.PUT, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")
            .body("""{"capacity":"1","date":"${newDate()}""")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.updateSession(routedRequest)
        //  Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `delete session, no auth`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1")
            .header("Content-Type", "application/json")
        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.deleteSession(routedRequest)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.UNAUTHORIZED, response.status)
    }

    @Test
    fun `delete session, not found`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/10")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.deleteSession(routedRequest)
        // Assert
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `delete session, should delete session`() {
        // Arrange
        val request = Request(Method.DELETE, "/sessions/1")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer 00000000-0000-0000-0000-000000000000")

        val routedRequest = RoutedRequest(request, UriTemplate.from("/sessions/{sid}"))
        // Act
        val response = api.deleteSession(routedRequest)
        // Assert
        assertEquals(Status.NO_CONTENT, response.status)
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
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(2u, session.gameSession.gid)
        assertEquals(100u, session.capacity)
        assertEquals(testDate1.toString(), session.date)
        assertEquals(1u, session.sid)
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
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(Status.NOT_FOUND, response.status)
    }

    @Test
    fun `test get session list should return sessions`() {
        // Arrange
        val request = Request(Method.GET, "/sessions?state=open")
        // Act
        val response = api.getSessionList(request)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchResultOutputModel>(sessionListJson)
        //  Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(2, sessionList.sessions.size)
        assertEquals(2u, sessionList.sessions[0].gameSession.gid)
        assertEquals(100u, sessionList.sessions[0].capacity)
        assertEquals(testDate1.toString(), sessionList.sessions[0].date)
        assertEquals(1u, sessionList.sessions[0].sid)
        assertEquals(2u, sessionList.sessions[1].gameSession.gid)
        assertEquals(100u, sessionList.sessions[1].capacity)
        assertEquals(testDate2.toString(), sessionList.sessions[1].date)
        assertEquals(2u, sessionList.sessions[1].sid)
        assertEquals(2, sessionList.total)
    }

    @Test
    fun `test get session list limit and skip should give session list`() {
        // Arrange
        val request = Request(Method.GET, "/sessions?limit=1&skip=1")
        // Act
        val response = api.getSessionList(request)
        val sessionListJson = response.bodyString()
        val sessionList = Json.decodeFromString<SessionSearchResultOutputModel>(sessionListJson)
        //  Assert
        assertEquals(Status.OK, response.status)
        assertEquals("application/json", response.header("Content-Type"))
        assertEquals(1, sessionList.sessions.size)
        assertEquals(2u, sessionList.sessions[0].gameSession.gid)
        assertEquals(100u, sessionList.sessions[0].capacity)
        assertEquals(testDate2.toString(), sessionList.sessions[0].date)
        assertEquals(2u, sessionList.sessions[0].sid)
        assertEquals(2, sessionList.total)
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

        private val testDate1 = currentLocalTime() + Duration.parse("PT1H")
        private val testDate2 = currentLocalTime() + Duration.parse("PT2H")

        fun newDate(): LocalDateTime = currentLocalTime() + Duration.parse("PT1H")

        fun setup() {

            val mockGame = Game(1u, "TestName".toName(), "TestDeveloper".toName(), setOf("RPG".toGenre()))
            val mockGame2 = Game(2u, "TestName123".toName(), "TestDeveloper123".toName(), setOf("RPG".toGenre()))

            val mockSession = Session(1u, 100u, testDate1, mockGame2, setOf())
            val mockSession2 = Session(2u, 100u, testDate2, mockGame2, setOf())

            val mockPlayer = Player(2u, "TestName".toName(), "testemail@test.pt".toEmail(), PasswordHash("TestPassword"))
            val mockPlayer2 = Player(3u, "TestName2".toName(), "testemail2@test.pt".toEmail(), PasswordHash("TestPassword"))

            storage.game.create(mockGame)
            storage.game.create(mockGame2)
            storage.session.create(mockSession)
            storage.session.create(mockSession2)
            storage.player.create(mockPlayer)
            storage.player.create(mockPlayer2)

            storage.session.addPlayer(mockSession.id, mockPlayer.id)
            storage.session.addPlayer(mockSession.id, mockPlayer2.id)
        }
    }
}
