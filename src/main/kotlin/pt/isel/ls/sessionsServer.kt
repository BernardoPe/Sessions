package pt.isel.ls.pt.isel.ls

import org.http4k.core.Method.*
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import pt.isel.ls.Services.gameService
import pt.isel.ls.Services.playerService
import pt.isel.ls.Services.sessionsService
import pt.isel.ls.WebApi.*

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")
class SessionsServer(api: SessionsApi) {

    private val requestProcessor = api::processRequest

    private val playerRoutes =
        routes(
            "players" bind POST to { req -> requestProcessor(req, "createPlayer", false) },
            "players/{pid}" bind GET to { req -> requestProcessor(req, "getPlayerDetails", false) }
        )

    private val gameRoutes =
        routes(
            "games" bind POST to { req -> requestProcessor(req, "createGame", true) },
            "games/{gid}" bind GET to { req -> requestProcessor(req, "getGameDetails", false) },
            "games" bind GET to { req -> requestProcessor(req, "getGameList", false) }

        )

    private val sessionRoutes =
        routes(
            "sessions" bind POST to { req -> requestProcessor(req, "createSession", true) },
            "sessions/{ssid}" bind PUT to { req -> requestProcessor(req, "addPlayerToSession", true) },
            "sessions/{ssid}" bind GET to { req -> requestProcessor(req, "getSessionDetails", false) },
            "sessions/{gid}/list" bind GET to { req -> requestProcessor(req, "getSessionList", false) }
        )

    val app =
        routes(
            playerRoutes,
            gameRoutes,
            sessionRoutes
        )

    private val jettyServer = app.asServer(Jetty(8080))
    fun start() {
        jettyServer.start()
        logger.info("Server started listening")
    }
    fun stop() {
        jettyServer.stop()
        logger.info("Server stopped listening")
    }

}


fun main() {
    val server = SessionsServer(SessionsApi(playerService(), gameService(), sessionsService()))
    server.start()
    readln()
    server.stop()
}

