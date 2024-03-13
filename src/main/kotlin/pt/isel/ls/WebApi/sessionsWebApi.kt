package pt.isel.ls.WebApi

import org.eclipse.jetty.server.session.Session
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import pt.isel.ls.Services.*
import pt.isel.ls.pt.isel.ls.logger


/**
 * The [SessionsApi] class is responsible for processing client requests and returning the appropriate response.
 *
 * The user should use the processRequest method, passing the HTTP request and the desired processor from the available list.
 *
 * @param playerServices The playerService instance
 * @param gameServices The gameService instance
 * @param sessionServices The sessionsService instance
 *
 * @property createPlayer The processor for creating a player
 * @property getPlayerDetails The processor for getting player details
 * @property createGame The processor for creating a game
 * @property getGameDetails The processor for getting game details
 * @property getGameList The processor for getting the game list
 * @property createSession The processor for creating a session
 * @property addPlayerToSession The processor for adding a player to a session
 * @property getSessionDetails The processor for getting session details
 * @property getSessionList The processor for getting the session list
 *
 * @property processRequest The method that processes the request and returns the response
 */

class SessionsApi(val playerServices: playerService,
                  val gameServices: gameService,
                  val sessionServices: sessionsService
) {

    val createPlayer : SessionsRequest = SessionsRequest(::createPlayer, false)
    val getPlayerDetails : SessionsRequest = SessionsRequest(::getPlayerDetails, false)
    val createGame : SessionsRequest = SessionsRequest(::createGame, true)
    val getGameDetails : SessionsRequest = SessionsRequest(::getGameDetails, false)
    val getGameList : SessionsRequest = SessionsRequest(::getGameList, false)
    val createSession : SessionsRequest = SessionsRequest(::createSession, true)
    val addPlayerToSession : SessionsRequest = SessionsRequest(::addPlayerToSession, true)
    val getSessionDetails : SessionsRequest = SessionsRequest(::getSessionDetails, false)
    val getSessionList : SessionsRequest = SessionsRequest(::getSessionList, false)

    private fun createPlayer(request: Request): Response {
        TODO()
    }

    private fun getPlayerDetails(request: Request): Response {
        TODO()
    }

    private fun createGame(request: Request): Response {
        TODO()
    }

    private fun getGameDetails(request: Request): Response {
        TODO()
    }

    private fun getGameList(request: Request): Response {
        TODO()
    }

    private fun createSession(request: Request): Response {
        TODO()
    }

    private fun addPlayerToSession(request: Request): Response {
        TODO()
    }

    private fun getSessionDetails(request: Request): Response {
        TODO()
    }

    private fun getSessionList(request: Request): Response {
        TODO()
    }

    /**
     * Processes the request and returns the response
     *
     * @param request The HTTP request
     * @param service The desired processor
     *
     * @return The response
     */
    fun processRequest(request: Request, service: SessionsRequest): Response {

        logRequest(request)

        val (processor, requiresAuth) = service

        if (request.header("content-type") != "application/json")
            return Response(BAD_REQUEST).header("content-type", "text/plain").body("Unsupported Media Type")

        if (requiresAuth && !verifyAuth(request))
            return Response(UNAUTHORIZED).header("content-type", "text/plain").body("Unauthorized")

        return try {
            processor(request)
        } catch (e: Exception) {
            // TODO ERROR HANDLING
            Response(OK).header("content-type", "text/plain").body(e.message ?: "Internal Server Error")
        }

    }

    private fun logRequest(request: Request) {
        logger.info(
            "incoming request: method={}, uri={}, content-type={} accept={}",
            request.method,
            request.uri,
            request.header("content-type"),
            request.header("accept"),
        )
    }

    private fun verifyAuth(request: Request) : Boolean {
        val token = request.header("Authorization")?.split(" ")?.get(1) ?: return false
        return playerServices.verifyToken(token)
    }

}

/**
 * The [SessionsRequest] represents a request to the Sessions Server.
 *
 * It is a pair of a request processor and a boolean that indicates if the request requires authentication.
 */
typealias SessionsRequest = Pair<(Request) -> Response, Boolean>
