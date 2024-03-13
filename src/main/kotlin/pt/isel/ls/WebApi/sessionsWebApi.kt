package pt.isel.ls.WebApi

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import pt.isel.ls.Services.*
import pt.isel.ls.pt.isel.ls.logger

class SessionsApi(val playerServices: playerService,
                  val gameServices: gameService,
                  val sessionServices: sessionsService
) {

    private val processors = mapOf(
        "createPlayer" to ::createPlayer,
        "getPlayerDetails" to ::getPlayerDetails,
        "createGame" to ::createGame,
        "getGameDetails" to ::getGameDetails,
        "getGameList" to ::getGameList,
        "createSession" to ::createSession,
        "addPlayerToSession" to ::addPlayerToSession,
        "getSessionDetails" to ::getSessionDetails,
        "getSessionList" to ::getSessionList,
    )

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

    fun processRequest(request: Request, service: String, requiresAuth: Boolean): Response {

        logRequest(request)

        if (requiresAuth && !verifyAuth(request))
            return Response(UNAUTHORIZED).header("content-type", "text/plain").body("Unauthorized")


        return try {
            val processFunction = processors[service]!!
            processFunction(request)
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

