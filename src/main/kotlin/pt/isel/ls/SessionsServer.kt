package pt.isel.ls

import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.PUT
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
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.storage.db.SessionsDataDBGame
import pt.isel.ls.storage.db.SessionsDataDBPlayer
import pt.isel.ls.storage.db.SessionsDataDBSession

val logger = LoggerFactory.getLogger("pt.isel.ls.http.HTTPServer")

const val PLAYER_ROUTE = "/players"
const val PLAYER_DETAILS_ROUTE = "/players/{pid}"
const val GAME_ROUTE = "/games"
const val GAME_DETAILS_ROUTE = "/games/{gid}"
const val SESSION_DETAILS_ROUTE = "/sessions/{sid}"
const val SESSION_PLAYER_ROUTE = "/sessions/{sid}/players"
const val SESSION_PLAYER_DETAILS_ROUTE = "/sessions/{sid}/players/{pid}"
const val SESSION_ROUTE = "/sessions"

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
            PLAYER_DETAILS_ROUTE bind GET to requestHandler::getPlayerDetails,
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
            SESSION_DETAILS_ROUTE bind PUT to requestHandler::updateSession,
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

    val storage = SessionsDataManager(
        SessionsDataDBGame(),
        SessionsDataDBPlayer(),
        SessionsDataDBSession(),
    )

    storage.use {
        val server = SessionsServer(
            SessionsApi(
                PlayerService(storage),
                GameService(storage),
                SessionsService(storage),
            ),
        )
        server.start()
        readln()
        server.stop()
    }

}
