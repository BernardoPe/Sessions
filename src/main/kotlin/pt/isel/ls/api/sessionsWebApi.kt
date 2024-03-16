package pt.isel.ls.api

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.routing.path
import pt.isel.ls.dto.*
import pt.isel.ls.exceptions.*
import pt.isel.ls.services.*


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

    private val processors = mapOf(
        Operation.CREATE_PLAYER to SessionsRequest(::createPlayer, false),
        Operation.GET_PLAYER_DETAILS to SessionsRequest(::getPlayerDetails, true),
        Operation.CREATE_GAME to SessionsRequest(::createGame, true),
        Operation.GET_GAME_DETAILS to SessionsRequest(::getGameById, true),
        Operation.GET_GAME_LIST to SessionsRequest(::getGameList, true),
        Operation.CREATE_SESSION to SessionsRequest(::createSession, true),
        Operation.ADD_PLAYER_TO_SESSION to SessionsRequest(::addPlayerToSession, true),
        Operation.GET_SESSION_DETAILS to SessionsRequest(::getSessionById, true),
        Operation.GET_SESSION_LIST to SessionsRequest(::getSessionList, true)
    )

    private fun createPlayer(request: Request): Response {
        val player = parseJsonBody<PlayerCreationInputModel>(request)
        val res = playerServices.createPlayer(player.name, player.email)
        return Response(CREATED)
            .header("content-type", "application/json")
            .body(Json.encodeToString(PlayerCreationOutputModel(res.first, res.second)))
    }

    private fun getPlayerDetails(request: Request): Response {
        val pid = request.path("pid")?.toIntOrNull() ?: throw BadRequestException("Invalid Player Identifier")
        val res = playerServices.getPlayerDetails(pid)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toInfoDTO()))
    }

    private fun createGame(request: Request): Response {
        val game = parseJsonBody<GameCreationInputModel>(request)
        val res = gameServices.createGame(game.name, game.developer, game.genres)
        return Response(CREATED)
            .header("content-type", "application/json")
            .body(Json.encodeToString(GameCreationOutputModel(res)))
    }

    private fun getGameById(request: Request): Response {
        val gid = request.path("gid")?.toIntOrNull() ?: throw BadRequestException("Invalid Game Identifier")
        val res = gameServices.getGameById(gid)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toInfoDTO()))
    }

    private fun getGameList(request: Request): Response {
        val (limit, skip) = (request.query("limit")?.toInt() ?: 5) to (request.query("skip")?.toInt() ?: 0)
        val gameSearch = parseJsonBody<GameSearchInputModel>(request)
        val res = gameServices.searchGames(gameSearch.genres, gameSearch.developer, limit, skip)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(GameSearchOutputModel(res.map { it.toInfoDTO() }.toSet())))
    }

    private fun createSession(request: Request): Response {
        val session = parseJsonBody<SessionCreationInputModel>(request)
        val res = sessionServices.createSession(session.capacity, session.gid, session.date)
        return Response(CREATED)
            .header("content-type", "application/json")
            .body(Json.encodeToString(SessionCreationOutputModel(res)))
    }

    private fun addPlayerToSession(request: Request): Response {
        val sid = request.path("sid")?.toIntOrNull() ?: throw BadRequestException("Invalid Session Identifier")
        val pid = request.path("pid")?.toIntOrNull() ?: throw BadRequestException("Invalid Player Identifier")
        val res = sessionServices.addPlayer(sid, pid)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(SessionAddPlayerOutputModel(res)))
    }

    private fun getSessionById(request: Request): Response {
        val sid = request.path("sid")?.toIntOrNull() ?: throw BadRequestException("Invalid Session Identifier")
        val res = sessionServices.getSessionById(sid)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toInfoDTO()))
    }

    private fun getSessionList(request: Request): Response {
        val (limit, skip) = (request.query("limit")?.toIntOrNull() ?: 5) to (request.query("skip")?.toIntOrNull() ?: 0)
        val sessionSearch = parseJsonBody<SessionSearchInputModel>(request)
        val res = sessionServices.listSessions(
            sessionSearch.gid,
            sessionSearch.date,
            sessionSearch.state,
            sessionSearch.pid,
            limit,
            skip
        )
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(SessionSearchOutputModel(res.map { it.toInfoDTO() }.toSet())))
    }

    /**
     * Processes the request and returns the response
     *
     * @param request The HTTP request
     * @param service The desired processor
     *
     * @return The response
     */
    fun processRequest(request: Request, service: Operation): Response {

        val (processor, requiresAuth) = processors[service] ?: return Response(INTERNAL_SERVER_ERROR)

        if (request.bodyString().isNotBlank() && request.header("content-type") != "application/json")
            return Response(BAD_REQUEST).header("content-type", "application/json").body(Json.encodeToString(UnsupportedMediaTypeException()))

        if (requiresAuth && !verifyAuth(request))
            return Response(UNAUTHORIZED).header("content-type", "application/json").body(Json.encodeToString(UnauthorizedException()))

        return try {
            processor(request)
        } catch (e: WebExceptions) {
            Response(Status(e.status, e.description)).header("content-type", "application/json").body(Json.encodeToString(e))
        }
        catch (e: Exception) {
            Response(INTERNAL_SERVER_ERROR).header("content-type", "application/json").body(Json.encodeToString(InternalServerErrorException()))
        }

    }

    private fun verifyAuth(request: Request) : Boolean {
        return true
        //val token = request.header("Authorization")?.split(" ")?.get(1) ?: return false
       // return playerServices.authenticatePlayer(token)
    }

    private inline fun <reified T> parseJsonBody(request: Request): T {
        val body = request.bodyString()
        return try {
            Json.decodeFromString<T>(body)
        } catch (e: SerializationException) {
            throw BadRequestException("Invalid Body")
        }
        catch (e: IllegalArgumentException) {
            throw BadRequestException(e.message)
        }
    }

}


/**
 * The [Operation] enum represents the available operations that the client can request.
 */
enum class Operation {
    CREATE_PLAYER,
    GET_PLAYER_DETAILS,
    CREATE_GAME,
    GET_GAME_DETAILS,
    GET_GAME_LIST,
    CREATE_SESSION,
    ADD_PLAYER_TO_SESSION,
    GET_SESSION_DETAILS,
    GET_SESSION_LIST
}

/**
 * The [SessionsRequest] represents a request to the Sessions Server.
 *
 * It is a pair of a request processor and a boolean that indicates if the request requires authentication.
 */
typealias SessionsRequest = Pair<(Request) -> Response, Boolean>
