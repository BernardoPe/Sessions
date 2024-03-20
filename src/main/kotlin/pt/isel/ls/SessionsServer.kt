package pt.isel.ls.pt.isel.ls

import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import pt.isel.ls.api.Operation
import pt.isel.ls.api.SessionsApi
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

const val PLAYER_ROUTE = "/players"
const val PLAYER_DETAILS_ROUTE = "/players/{pid}"
const val GAME_ROUTE = "/games"
const val GAME_DETAILS_ROUTE = "/games/{gid}"
const val SESSION_ROUTE = "/sessions"
const val SESSION_DETAILS_ROUTE = "/sessions/{sid}"
const val SESSION_PLAYER_ROUTE = "/sessions/{sid}/players"


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

    private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private fun dispatcher(req: Request, operation: Operation): Response
    = executor.submit<Response> {
        logRequest(req)
        requestHandler(req, operation).also { logResponse(it) }
    }.get()

    private fun bindRoute(route: String, method: org.http4k.core.Method, operation: Operation) =
        route bind method to { req -> dispatcher(req, operation) }

    private val playerRoutes =
        routes(
            bindRoute(PLAYER_ROUTE, POST, Operation.CREATE_PLAYER),
            bindRoute(PLAYER_DETAILS_ROUTE, GET, Operation.GET_PLAYER_DETAILS)
        )

    private val gameRoutes =
        routes(
            bindRoute(GAME_ROUTE, POST, Operation.CREATE_GAME),
            bindRoute(GAME_DETAILS_ROUTE, GET, Operation.GET_GAME_DETAILS),
            bindRoute(GAME_ROUTE, GET, Operation.GET_GAME_LIST)
        )

    private val sessionRoutes =
        routes(
            bindRoute(SESSION_ROUTE, POST, Operation.CREATE_SESSION),
            bindRoute(SESSION_PLAYER_ROUTE, PUT, Operation.ADD_PLAYER_TO_SESSION),
            bindRoute(SESSION_DETAILS_ROUTE, GET, Operation.GET_SESSION_DETAILS),
            bindRoute(SESSION_ROUTE, GET, Operation.GET_SESSION_LIST)
        )

    val sessionsHandler =
        routes(
            playerRoutes,
            gameRoutes,
            sessionRoutes
        )

    private fun logRequest(request: Request) {
        logger.info(
            "incoming request: method={}, uri={}, content-type={} accept={}",
            request.method,
            request.uri,
            request.header("content-type"),
            request.header("accept"),
        )
    }

    private fun logResponse(response: Response) {
        logger.info(
            "outgoing response: status={}, content-type={}",
            response.status,
            response.header("content-type"),
        )
    }


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
        executor.shutdown()
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow()
            }
        } catch (e: InterruptedException) {
            executor.shutdownNow()
        }
        logger.info("Server stopped listening")
    }
}


fun main() {
    val server = SessionsServer(
        SessionsApi(
            PlayerService(SessionsDataMemPlayer()),
            GameService(SessionsDataMemGame()),
            SessionsService(SessionsDataMemSession(), SessionsDataMemGame(), SessionsDataMemPlayer())
            // Must modify services in later times or stick with this implementation
        )
    )
    server.start()
    readln()
    server.stop()
}
