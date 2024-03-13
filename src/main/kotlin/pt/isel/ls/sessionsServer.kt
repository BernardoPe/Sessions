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

const val PLAYER_ROUTE = "/players"
const val PLAYER_DETAILS_ROUTE = "/players/{pid}"
const val GAME_ROUTE = "/games"
const val GAME_DETAILS_ROUTE = "/games/{gid}"
const val SESSION_ROUTE = "/sessions"
const val SESSION_DETAILS_ROUTE = "/sessions/{ssid}"
const val SESSION_LIST_ROUTE = "/sessions/{gid}/list"


/**
 * The [SessionsServer] class is responsible for starting and stopping the server.
 *
 * The user should use the start method to start the server and the stop method to stop the server.
 *
 * @param api The [SessionsApi] instance
 * @param port The port where the server will listen
 *
 * @property start The method that starts the server
 * @property stop The method that stops the server
 */
class SessionsServer(api: SessionsApi, port: Int = 8080) {

    private val requestHandler = api::processRequest

    private val playerRoutes =
        routes(
            PLAYER_ROUTE bind POST to { req -> requestHandler(req, api.createPlayer) },
            PLAYER_DETAILS_ROUTE bind GET to { req -> requestHandler(req, api.getPlayerDetails) }
        )

    private val gameRoutes =
        routes(
            GAME_ROUTE bind POST to { req -> requestHandler(req, api.createGame) },
            GAME_DETAILS_ROUTE bind GET to { req -> requestHandler(req, api.getGameDetails) },
            GAME_ROUTE bind GET to { req -> requestHandler(req, api.getGameList) }
        )

    private val sessionRoutes =
        routes(
            SESSION_ROUTE bind POST to { req -> requestHandler(req, api.createSession) },
            SESSION_DETAILS_ROUTE bind PUT to { req -> requestHandler(req, api.addPlayerToSession) },
            SESSION_DETAILS_ROUTE bind GET to { req -> requestHandler(req, api.getSessionDetails) },
            SESSION_LIST_ROUTE bind GET to { req -> requestHandler(req, api.getSessionList) }
        )

    val sessionsHandler =
        routes(
            playerRoutes,
            gameRoutes,
            sessionRoutes
        )

    private val jettyServer = sessionsHandler.asServer(Jetty(port))

    /**
     * The method that starts the server
     */
    fun start() {
        jettyServer.start()
        logger.info("Server started listening")
    }

    /**
     * The method that stops the server
     */
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

