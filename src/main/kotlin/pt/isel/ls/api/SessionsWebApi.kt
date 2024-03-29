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
import pt.isel.ls.data.domain.Email
import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.session.toState
import pt.isel.ls.data.mapper.*
import pt.isel.ls.dto.*
import pt.isel.ls.exceptions.*
import pt.isel.ls.pt.isel.ls.logger
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.utils.toLocalDateTime
import pt.isel.ls.utils.toUInt
import java.util.*

/**
 * The [SessionsApi] class is responsible for processing HTTP requests and returning responses.
 *
 *
 *
 * @param playerServices The [PlayerService] instance
 * @param gameServices The [GameService] instance
 * @param sessionServices The [SessionsService] instance
 *
 * @property createPlayer The method to create a player
 * @property getPlayerDetails The method to get player details
 * @property createGame The method to create a game
 * @property getGameById The method to get a game by its identifier
 * @property getGameList The method to get a list of games
 * @property createSession The method to create a session
 * @property addPlayerToSession The method to add a player to a session
 * @property getSessionById The method to get a session by its identifier
 * @property getSessionList The method to get a list of sessions
 *
 *
 */

class SessionsApi(
    private val playerServices: PlayerService,
    private val gameServices: GameService,
    private val sessionServices: SessionsService,
) {

    fun createPlayer(request: Request): Response = processRequest(request) {
        val player = parseJsonBody<PlayerCreationInputModel>(request)
        val res = playerServices.createPlayer(Name(player.name), Email(player.email))

        Response(CREATED)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toPlayerCreationDTO()))
    }

    fun getPlayerDetails(request: Request) = processRequest(request) {
        val pid = request.path("pid")?.toUInt("Player Identifier") ?: throw BadRequestException("No Player Identifier provided")
        val res = playerServices.getPlayerDetails(pid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toPlayerInfoDTO()))
    }

    fun createGame(request: Request) = authHandler(request) {
        val game = parseJsonBody<GameCreationInputModel>(request)
        val res = gameServices.createGame(Name(game.name), Name(game.developer), game.genres.map { Genre(it) }.toSet())

        Response(CREATED)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toGameCreationDTO()))
    }

    fun getGameById(request: Request) = processRequest(request) {
        val gid = request.path("gid")?.toUInt("GameIdentifier") ?: throw BadRequestException("No Game Identifier provided")
        val res = gameServices.getGameById(gid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toGameInfoDTO()))
    }

    fun getGameList(request: Request) = processRequest(request) {
        val (limit, skip) = (request.query("limit")?.toUInt("Limit") ?: 5u) to (request.query("skip")?.toUInt("Skip") ?: 0u)

        val genres = request.query("genres")?.split(",") ?: throw BadRequestException("No Genres provided")
        val developer = request.query("developer") ?: throw BadRequestException("No Developer provided")

        val res = gameServices.searchGames(
            genres.map { Genre(it) }.toSet(),
            Name(developer),
            limit,
            skip,
        )

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toGameSearchDTO()))
    }

    fun createSession(request: Request) = authHandler(request) {
        val session = parseJsonBody<SessionCreationInputModel>(request)
        val res = sessionServices.createSession(session.capacity, session.gid, session.date.toLocalDateTime())

        Response(CREATED)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toSessionCreationDTO()))
    }

    fun addPlayerToSession(request: Request) = authHandler(request) {
        val sid = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
        val player = parseJsonBody<SessionAddPlayerInputModel>(request)
        val res = sessionServices.addPlayer(sid, player.pid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toSessionOperationMessage()))
    }

    fun removePlayerFromSession(request: Request) = authHandler(request) {
        val sid = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
        val pid = request.path("pid")?.toUInt("Player Identifier") ?: throw BadRequestException("No Player Identifier provided")
        val res = sessionServices.removePlayer(sid, pid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toSessionOperationMessage()))
    }

    fun updateSession(request: Request) = authHandler(request) {
        val session = parseJsonBody<SessionUpdateInputModel>(request)
        val sessionId = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
        val res = sessionServices.updateSession(sessionId, session.capacity, session.date.toLocalDateTime())

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toSessionOperationMessage()))
    }

    fun deleteSession(request: Request): Response = authHandler(request) {
        val sid = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
        val res = sessionServices.deleteSession(sid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toSessionOperationMessage()))
    }

    fun getSessionById(request: Request) = processRequest(request) {
        val sid = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
        val res = sessionServices.getSessionById(sid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toSessionInfoDTO()))
    }

    fun getSessionList(request: Request) = processRequest(request) {
        val (limit, skip) = (request.query("limit")?.toUInt("Limit") ?: 5u) to (request.query("skip")?.toUInt("Skip") ?: 0u)
        val gid = request.path("gid")?.toUInt("Game Identifier") ?: throw BadRequestException("No Game Identifier provided")

        val res = sessionServices.listSessions(
            gid,
            request.query("date")?.toLocalDateTime(),
            request.query("state")?.toState(),
            request.query("pid")?.toUInt("Player Identifier"),
            limit,
            skip,
        )

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toSessionSearchDTO()))
    }

    /**
     * Handles the authentication and processes the request
     * @param request The HTTP request
     * @param service The desired processor
     */

    private fun authHandler(request: Request, service: (Request) -> Response): Response {
        logger.info("Authenticating request")
        return if (verifyAuth(request)) {
            logger.info("Authorized request")
            processRequest(request, service)
        } else {
            logger.info("Unauthorized request")
            Response(UNAUTHORIZED).header("content-type", "application/json").body(
                Json.encodeToString(
                    UnauthorizedException(),
                ),
            )
        }
    }

    /**
     * Processes the request and returns the response
     *
     * @param request The HTTP request
     * @param service The desired processor
     *
     * @return The response
     */
    private fun processRequest(request: Request, service: (Request) -> Response): Response {
        logRequest(request)

        if (request.bodyString().isNotBlank() && request.header("content-type") != "application/json") {
            return Response(BAD_REQUEST).header("content-type", "application/json").body(
                Json.encodeToString(
                    UnsupportedMediaTypeException(),
                ),
            )
        }

        val res = try {
            service(request)
        } catch (e: SessionsExceptions) {
            Response(Status(e.status, e.description)).header("content-type", "application/json").body(Json.encodeToString(e))
        } catch (e: IllegalArgumentException) {
            Response(BAD_REQUEST).header("content-type", "application/json").body(Json.encodeToString(SessionsExceptions(BAD_REQUEST.code, "Bad Request", e.message)))
        } catch (e: Exception) {
            logger.error(e.message, e)
            Response(INTERNAL_SERVER_ERROR).header("content-type", "application/json").body(
                Json.encodeToString(InternalServerErrorException()),
            )
        }

        return res.also { logResponse(it) }
    }

    private inline fun <reified T> parseJsonBody(request: Request): T {
        val body = request.bodyString()
        return try {
            Json.decodeFromString<T>(body)
        } catch (e: SerializationException) {
            throw BadRequestException("Invalid Body")
        } catch (e: IllegalArgumentException) {
            throw BadRequestException(e.message)
        }
    }

    private fun verifyAuth(request: Request): Boolean {
        return try {
            val token = UUID.fromString(
                request.header("Authorization")?.split(" ")?.get(1) ?: return false,
            )
            playerServices.authenticatePlayer(token)
        } catch (e: Exception) {
            false
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

    private fun logResponse(response: Response) {
        logger.info(
            "outgoing response: status={}, content-type={}",
            response.status,
            response.header("content-type"),
        )
    }
}
