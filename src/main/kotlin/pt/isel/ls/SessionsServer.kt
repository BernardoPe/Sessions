package pt.isel.ls

import org.http4k.core.Method.PATCH
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import pt.isel.ls.api.SessionsApi
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.storage.DBManager

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

const val PLAYER_ROUTE = "/players"
const val PLAYER_DETAILS_ROUTE = "/players/{pid}"
const val GAME_ROUTE = "/games"
const val GAME_DETAILS_ROUTE = "/games/{gid}"
const val SESSION_DETAILS_ROUTE = "/sessions/{sid}"
const val SESSION_PLAYER_ROUTE = "/sessions/{sid}/players"
const val SESSION_PLAYER_DETAILS_ROUTE = "/sessions/{sid}/players/{pid}"
const val SESSION_ROUTE = "/sessions"
const val AUTH_ROUTE = "/auth"
const val PLAYER_LOGIN_ROUTE = "/players/login"
const val LOGOUT_ROUTE = "/logout"

/**
 * The [SessionsServer] class is responsible for starting and stopping the server.
 *
 * The user should use the start method to start the server and the stop method to stop the server.
 *
 * @param requestHandler The [SessionsApi] instance
 * @param port The port where the server will listen
 *
 * @property start The method that starts the server
 * @property stop The method that stops the server
 */
class SessionsServer(requestHandler: SessionsApi, port: Int = 8080) {

    private val playerRoutes =
        routes(
            PLAYER_ROUTE bind POST to requestHandler::createPlayer,
            PLAYER_ROUTE bind GET to requestHandler::getPlayerList,
            PLAYER_DETAILS_ROUTE bind GET to requestHandler::getPlayerDetails,
            AUTH_ROUTE bind POST to requestHandler::authPlayer,
            PLAYER_LOGIN_ROUTE bind POST to requestHandler::loginPlayer,
            LOGOUT_ROUTE bind GET to requestHandler::playerLogout,
        )

    private val gameRoutes =
        routes(
            GAME_ROUTE bind GET to requestHandler::getGameList,
            GAME_DETAILS_ROUTE bind GET to requestHandler::getGameById,
            GAME_ROUTE bind POST to requestHandler::createGame,
        )

    private val sessionRoutes =
        routes(
            SESSION_ROUTE bind POST to requestHandler::createSession,
            SESSION_PLAYER_ROUTE bind POST to requestHandler::addPlayerToSession,
            SESSION_PLAYER_DETAILS_ROUTE bind DELETE to requestHandler::removePlayerFromSession,
            SESSION_DETAILS_ROUTE bind GET to requestHandler::getSessionById,
            SESSION_DETAILS_ROUTE bind DELETE to requestHandler::deleteSession,
            SESSION_DETAILS_ROUTE bind PATCH to requestHandler::updateSession,
            SESSION_ROUTE bind GET to requestHandler::getSessionList,
        )

    val sessionsHandler =
        routes(
            playerRoutes,
            gameRoutes,
            sessionRoutes,
            singlePageApp(ResourceLoader.Directory("static-content")),
        )

    private val jettyServer = sessionsHandler.asServer(Jetty(port))

    /**
     * The method that starts the server
     * This method starts the server and adds a shutdown hook to stop the server when the application is closed
     *
     */
    fun start() {
        jettyServer.start()
        logger.info("Server started listening")
        Runtime.getRuntime().addShutdownHook(Thread{
            logger.info("Shutting down server")
            stop()
        })
    }

    /**
     * The method that stops the server
     */
    fun stop() {
        jettyServer.stop()
    }

    /**
     * The method that blocks the current thread until the server stops
     */
    fun join() {
        jettyServer.block()
    }

}

fun main() {

    val databaseURL = System.getenv("JDBC_PRODUCTION_DATABASE_URL")

    val storage = DBManager(databaseURL)

    storage.use {
        val server = SessionsServer(
            SessionsApi(
                PlayerService(storage),
                GameService(storage),
                SessionsService(storage),
            ),
        )
        server.start()
        server.join()
    }

}
