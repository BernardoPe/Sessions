package pt.isel.ls.Server

import kotlinx.serialization.json.Json
import org.http4k.core.*
import org.junit.After
import org.junit.Before
import pt.isel.ls.DTO.Game.Game
import pt.isel.ls.DTO.Player.Player
import pt.isel.ls.Services.gameService
import pt.isel.ls.Services.playerService
import pt.isel.ls.Services.sessionsService
import pt.isel.ls.pt.isel.ls.SessionsServer
import pt.isel.ls.WebApi.SessionsApi
import kotlin.test.Test

/**
 *
 *
 *  private val playerRoutes =
 *         routes(
 *             "players" bind POST to { req -> requestProcessor(req, "createPlayer", false) },
 *             "players/{pid}" bind GET to { req -> requestProcessor(req, "getPlayerDetails", false) }
 *         )
 *
 *     private val gameRoutes =
 *         routes(
 *             "games" bind POST to { req -> requestProcessor(req, "createGame", true) },
 *             "games/{gid}" bind GET to { req -> requestProcessor(req, "getGameDetails", false) },
 *             "games" bind GET to { req -> requestProcessor(req, "getGameList", false) }
 *
 *         )
 *
 *     private val sessionRoutes =
 *         routes(
 *             "sessions" bind POST to { req -> requestProcessor(req, "createSession", true) },
 *             "sessions/{ssid}" bind PUT to { req -> requestProcessor(req, "addPlayerToSession", true) },
 *             "sessions/{ssid}" bind GET to { req -> requestProcessor(req, "getSessionDetails", false) },
 *             "sessions/{gid}/list" bind GET to { req -> requestProcessor(req, "getSessionList", false) }
 *         )
 *
 *
 *
 *
 */
class ServerTest {

    private val server = SessionsServer(SessionsApi(playerService(), gameService(), sessionsService()))

    @Before
    fun startServer() {
        server.start()
    }

    @After
    fun stopServer() {
        server.stop()
    }
    @Test
    fun `test create player`() {
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","email":Test""")
        val response = server.app(request)
        assert(response.status == Status.CREATED)
    }
    @Test
    fun `test create player with invalid body`() {
        val request = Request(Method.POST, "/players")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test",""")
        val response = server.app(request)
        assert(response.status == Status.BAD_REQUEST)
    }
    @Test
    fun `test get player details`() {
        val request = Request(Method.GET, "/players/1")
        val response = server.app(request)
        assert(response.status == Status.OK)
        val playerDetailsJson = response.bodyString()
        val playerDetails = Json.decodeFromString<Player>(playerDetailsJson)
        assert(playerDetails.name == "TestName")
        assert(playerDetails.email == "TestEmail")
        assert(playerDetails.pid == 1)
    }
    @Test
    fun `test get player details not found`() {
        val request = Request(Method.GET, "/players/2")
        val response = server.app(request)
        assert(response.status == Status.NOT_FOUND)
    }
    @Test
    fun `test create game`() {
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","description":"Test","genres":["Test"]}""")
        val response = server.app(request)
        assert(response.status == Status.CREATED)
    }
    @Test
    fun `test create game with invalid body`() {
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test"}""")
        val response = server.app(request)
        assert(response.status == Status.BAD_REQUEST)
    }

    @Test
    fun `test create game no auth`() {
        val request = Request(Method.POST, "/games")
            .header("Content-Type", "application/json")
            .body("""{"name":"Test","description":"Test","genres":["Test"]}""")
        val response = server.app(request)
        assert(response.status == Status.UNAUTHORIZED)
    }
    @Test
    fun `test get game details`() {
        val request = Request(Method.GET, "/games/1")
        val response = server.app(request)
        assert(response.status == Status.OK)
        val gameDetailsJson = response.bodyString()
        val gameDetails = Json.decodeFromString<Game>(gameDetailsJson)
        assert(gameDetails.name == "TestName")
        assert(gameDetails.developer == "TestDeveloper")
        assert(gameDetails.genres == listOf("TestGenre"))
        assert(gameDetails.gid == 1)
    }
    @Test
    fun `test get game details not found`() {
        val request = Request(Method.GET, "/games/2")
        val response = server.app(request)
        assert(response.status == Status.NOT_FOUND)
    }

    // todo more tests



}