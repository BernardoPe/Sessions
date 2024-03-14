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
import pt.isel.ls.WebApi.Operation
import pt.isel.ls.WebApi.SessionsApi

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

const val PLAYER_ROUTE = "/players"
const val PLAYER_DETAILS_ROUTE = "/players/{pid}"
const val GAME_ROUTE = "/games"
const val GAME_DETAILS_ROUTE = "/games/{gid}"
const val SESSION_ROUTE = "/sessions"
const val SESSION_DETAILS_ROUTE = "/sessions/{sid}"

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


    //TODO multi-threading

    private val requestHandler = api::processRequest

    private val playerRoutes =
        routes(
            PLAYER_ROUTE bind POST to { req -> requestHandler(req, Operation.CREATE_PLAYER) },
            PLAYER_DETAILS_ROUTE bind GET to { req -> requestHandler(req, Operation.GET_PLAYER_DETAILS) }
        )

    private val gameRoutes =
        routes(
            GAME_ROUTE bind POST to { req -> requestHandler(req, Operation.CREATE_GAME) },
            GAME_DETAILS_ROUTE bind GET to { req -> requestHandler(req, Operation.GET_GAME_DETAILS) },
            GAME_ROUTE bind GET to { req -> requestHandler(req, Operation.GET_GAME_LIST) }
        )

    private val sessionRoutes =
        routes(
            SESSION_ROUTE bind POST to { req -> requestHandler(req, Operation.CREATE_SESSION) },
            SESSION_DETAILS_ROUTE bind PUT to { req -> requestHandler(req, Operation.ADD_PLAYER_TO_SESSION) },
            SESSION_DETAILS_ROUTE bind GET to { req -> requestHandler(req, Operation.GET_SESSION_DETAILS) },
            SESSION_ROUTE bind GET to { req -> requestHandler(req, Operation.GET_SESSION_LIST) }
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

