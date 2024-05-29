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
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.SameSite
import org.http4k.core.cookie.cookie
import org.http4k.routing.path
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.data.domain.primitives.Password
import pt.isel.ls.data.domain.session.toState
import pt.isel.ls.data.dto.GameCreationInputModel
import pt.isel.ls.data.dto.PlayerCreationInputModel
import pt.isel.ls.data.dto.PlayerLoginInputModel
//import pt.isel.ls.data.dto.PlayerLoginInputModel
import pt.isel.ls.data.mapper.toGameCreationDTO
import pt.isel.ls.data.mapper.toGameInfoDTO
import pt.isel.ls.data.mapper.toGameSearchDTO
import pt.isel.ls.data.mapper.toPlayerCredentialsDTO
import pt.isel.ls.data.mapper.toPlayerInfoDTO
import pt.isel.ls.data.mapper.toPlayerSearchDTO
import pt.isel.ls.data.mapper.toSessionCreationDTO
import pt.isel.ls.data.mapper.toSessionInfoDTO
import pt.isel.ls.data.mapper.toSessionSearchDTO
import pt.isel.ls.dto.SessionAddPlayerInputModel
import pt.isel.ls.dto.SessionCreationInputModel
import pt.isel.ls.dto.SessionUpdateInputModel
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.InternalServerErrorException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.exceptions.SessionsExceptions
import pt.isel.ls.exceptions.UnauthorizedException
import pt.isel.ls.exceptions.UnsupportedMediaTypeException
import pt.isel.ls.logger
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.utils.between
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.toLocalDateTime
import pt.isel.ls.utils.toUInt
import java.net.URLDecoder
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

const val DEFAULT_LIMIT = 5u
const val DEFAULT_SKIP = 0u

class SessionsApi(
    private val playerServices: PlayerService,
    private val gameServices: GameService,
    private val sessionServices: SessionsService,
) {


    /**
     * Creates a player
     * @param request The HTTP request
     * @return [CREATED] if the player was created
     * @throws BadRequestException If the request body is invalid
     */

    fun createPlayer(request: Request): Response = processRequest(request) {
        val player = parseJsonBody<PlayerCreationInputModel>(request)
        val res = playerServices.createPlayer(Name(player.name), Email(player.email), Password(player.password))

        Response(CREATED)
            .header("content-type", "application/json")
            .header("location", "/players/${res.first}")
            .cookie(createCookie(null, res.second.token))
            .body(Json.encodeToString(res.toPlayerCredentialsDTO()))
    }

    /**
     * Gets a player by its identifier
     * @param request The HTTP request
     * @return [OK] with the player details if the player is found
     * @throws BadRequestException If the player identifier is not provided
     * @throws NotFoundException If the player is not found
     */
    fun getPlayerDetails(request: Request) = processRequest(request) {
        val pid = request.path("pid")?.toUInt("Player Identifier") ?: throw BadRequestException("No Player Identifier provided")
        val res = playerServices.getPlayerDetails(pid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toPlayerInfoDTO()))
    }

    fun loginPlayer(request: Request) = processRequest(request) {
        val player = parseJsonBody<PlayerLoginInputModel>(request)
        val res = playerServices.loginPlayer(Name(player.name), Password(player.password))

        Response(OK)
            .header("content-type", "application/json")
            .cookie(createCookie(null, res.second.token))
            .body(Json.encodeToString(res.toPlayerCredentialsDTO()))
    }

    /**
     * Authenticates a player
     * @param request The HTTP request
     * @return [OK] if the player is authenticated
     * @throws BadRequestException If the request is invalid
     */

    fun authPlayer(request: Request): Response = processRequest(request) {

        val token = parseCookie(request)

        val playerAndToken = playerServices.authenticatePlayer(token)

        if (playerAndToken != null) {
            Response(OK)
                .header("content-type", "application/json")
                .cookie(createCookie(null, playerAndToken.second.token))
                .body(Json.encodeToString(playerAndToken.first.toPlayerInfoDTO()))
        } else {
            Response(UNAUTHORIZED)
                .header("content-type", "application/json")
                .body(Json.encodeToString(UnauthorizedException()))
        }

    }

    /**
     * Logs out a player
     */
    fun playerLogout(request: Request) = processRequest(request) {

        val token = parseCookie(request)

        if (playerServices.authenticatePlayer(token) == null) {
            throw UnauthorizedException()
        }

        if (!playerServices.logoutPlayer(token)) {
            throw InternalServerErrorException()
        }

        Response(OK)
            .header("content-type", "application/json")
            .cookie(createCookie(0, null))
    }

    /**
     * Gets a list of players
     *
     * This operation has the following query parameters:
     * - name: The player name
     * - limit: The maximum number of players to return
     * - skip: The number of players to skip
     * @param request The HTTP request
     * @return [OK] with the list of players or [NO_CONTENT] if the list is empty
     * @throws BadRequestException If there are invalid parameters
     *
     */

    fun getPlayerList(request: Request) = processRequest(request) {
        val (limit, skip) = (request.query("limit")?.toUInt("Limit") ?: DEFAULT_LIMIT) to (request.query("skip")
            ?.toUInt("Skip")
            ?: DEFAULT_SKIP)
        val name = request.query("name")?.parseURLEncodedString()
        val res = playerServices.getPlayerList(
            name?.let { Name(it) },
            limit,
            skip
        )
        if (res.first.isEmpty())
            Response(NO_CONTENT)
        else {
            Response(OK)
                .header("content-type", "application/json")
                .body(Json.encodeToString(res.toPlayerSearchDTO()))
        }
    }


    /**
     * Creates a game
     * @param request The HTTP request
     * @return [CREATED] if the game was created
     * @throws BadRequestException If the request body is invalid
     */

    fun createGame(request: Request) = authHandler(request) {
        val game = parseJsonBody<GameCreationInputModel>(request)
        val res = gameServices.createGame(Name(game.name), Name(game.developer), game.genres.map { Genre(it) }.toSet())

        Response(CREATED)
            .header("content-type", "application/json")
            .header("location", "/games/${res}")
            .body(Json.encodeToString(res.toGameCreationDTO()))
    }

    /**
     * Gets a game by its identifier
     * @param request The HTTP request
     * @return The result with the game
     * @throws BadRequestException If the game identifier is not provided
     * @throws NotFoundException If the game is not found
     */
    fun getGameById(request: Request) = processRequest(request) {
        val gid = request.path("gid")?.toUInt("GameIdentifier") ?: throw BadRequestException("No Game Identifier provided")
        val res = gameServices.getGameById(gid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toGameInfoDTO()))
    }

    /**
     * Gets a list of games
     *
     * This operation has the following query parameters:
     * - genres: The genres of the games
     * - developer: The developer of the games
     * - name: The name of the games
     * - limit: The maximum number of games to return
     * - skip: The number of games to skip
     * @param request The HTTP request
     * @return [OK] with the list of games or [NO_CONTENT] if the list is empty
     * @throws BadRequestException If there are invalid parameters
     */
    fun getGameList(request: Request) = processRequest(request) {
        val (limit, skip) = (request.query("limit")?.toUInt("Limit") ?: DEFAULT_LIMIT) to (request.query("skip")
            ?.toUInt("Skip") ?: DEFAULT_SKIP)

        val genres = request.query("genres")?.parseURLEncodedString()
                                                  ?.split(',')
                                                  ?.map { it.trim() }

        val developer = request.query("developer")?.parseURLEncodedString()
        val name = request.query("name")?.parseURLEncodedString()

        val res = gameServices.searchGames(
            genres?.map { Genre(it) }?.toSet(),
            developer?.let { Name(it) },
            name?.let { Name(it) },
            limit,
            skip,
        )
        if (res.first.isEmpty())
            Response(NO_CONTENT)
        else
            Response(OK)
                .header("content-type", "application/json")
                .body(Json.encodeToString(res.toGameSearchDTO()))

    }

    /**
     * Creates a session
     * @param request The HTTP request
     * @return [CREATED] if the session was created
     * @throws BadRequestException If the request body is invalid
     * @throws NotFoundException If the game is not found
     */

    fun createSession(request: Request) = authHandler(request) {
        val session = parseJsonBody<SessionCreationInputModel>(request)
        val res = sessionServices.createSession(session.capacity, session.gid, session.date.toLocalDateTime())

        Response(CREATED)
            .header("content-type", "application/json")
            .header("location", "/sessions/${res}")
            .body(Json.encodeToString(res.toSessionCreationDTO()))
    }

    /**
     * Adds a player to a session
     * @param request The HTTP request
     * @return [CREATED] if the player was added
     * @throws BadRequestException If the session identifier or the player identifier is not provided or the request body is invalid
     * @throws NotFoundException If the session or the player is not found
     */
    fun addPlayerToSession(request: Request) = authHandler(request) {
        val sid = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
        val player = parseJsonBody<SessionAddPlayerInputModel>(request)
        sessionServices.addPlayer(sid, player.pid)

        Response(CREATED)
            .header("location", "/sessions/$sid/players/${player.pid}")
    }

    /**
     * Removes a player from a session
     * @param request The HTTP request
     * @return [OK] if the player was removed
     * @throws BadRequestException If the session identifier or the player identifier is not provided
     * @throws NotFoundException If the session or the player is not found
     */

    fun removePlayerFromSession(request: Request) = authHandler(request) {
        val sid = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
        val pid = request.path("pid")?.toUInt("Player Identifier") ?: throw BadRequestException("No Player Identifier provided")
        sessionServices.removePlayer(sid, pid)

        Response(OK)
    }

    /**
     * Updates a session by its identifier
     * @param request The HTTP request
     * @return [OK] if the session was updated
     * @throws BadRequestException If the session identifier is not provided or the body is invalid
     * @throws NotFoundException If the session is not found
     */

    fun updateSession(request: Request) = authHandler(request) {
        val session = parseJsonBody<SessionUpdateInputModel>(request)

        if (session.capacity == null && session.date == null) {
            Response(NO_CONTENT)
        } else {
            val sessionId = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
            sessionServices.updateSession(sessionId, session.capacity, session.date?.toLocalDateTime())
            Response(NO_CONTENT)
        }

    }

    /**
     * Deletes a session by its identifier
     * @param request The HTTP request
     * @return [OK] if the session was deleted
     * @throws BadRequestException If the session identifier is not provided
     * @throws NotFoundException If the session is not found
     */
    fun deleteSession(request: Request): Response = authHandler(request) {
        val sid = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")

        sessionServices.deleteSession(sid)

        Response(OK)
    }

    /**
     * Gets a session by its identifier
     * @param request The HTTP request
     * @return The result with the session
     * @throws BadRequestException If the session identifier is not provided
     * @throws NotFoundException If the session is not found
     */
    fun getSessionById(request: Request) = processRequest(request) {
        val sid = request.path("sid")?.toUInt("Session Identifier") ?: throw BadRequestException("No Session Identifier provided")
        val res = sessionServices.getSessionById(sid)

        Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(res.toSessionInfoDTO()))
    }

    /**
     * Gets a list of sessions
     *
     * This operation has the following query parameters:
     * - gid: The game identifier
     * - date: The date of the session
     * - state: The state of the session
     * - pid: The player identifier
     * - limit: The maximum number of sessions to return
     * - skip: The number of sessions to skip
     * @param request The HTTP request
     * @return [OK] with the list of sessions or [NO_CONTENT] if the list is empty
     * @throws BadRequestException If there are invalid parameters
     */

    fun getSessionList(request: Request) = processRequest(request) {
        val (limit, skip) = (request.query("limit")?.toUInt("Limit") ?: DEFAULT_LIMIT) to (request.query("skip")
            ?.toUInt("Skip") ?: DEFAULT_SKIP)

        val res = sessionServices.listSessions(
            request.query("gid")?.toUInt("Game Identifier"),
            request.query("date")?.parseURLEncodedString().let { it?.toLocalDateTime() },
            request.query("state")?.toState(),
            request.query("pid")?.toUInt("Player Identifier"),
            limit,
            skip,
        )
        if (res.first.isEmpty())
            Response(NO_CONTENT)
        else
            Response(OK)
                .header("content-type", "application/json")
                .body(Json.encodeToString(res.toSessionSearchDTO()))
    }

    /**
     * Handles the authentication and processes the request
     * @param request The HTTP request
     * @param service The desired processor
     * @return The response of the request or an unauthorized response
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

    private fun parseCookie(request: Request): UUID {
        val cookie = request.cookie("Authorization")?.value ?: throw UnauthorizedException()
        return UUID.fromString(cookie)
    }

    private fun createCookie(expiration: Long?, token: UUID?): Cookie {
        return Cookie(
            "Authorization",
            token.toString(),
            maxAge = expiration,
            sameSite = SameSite.Strict,
            secure = true,
            httpOnly = true,
            path = "/",
        )
    }

    private fun verifyAuth(request: Request): Boolean {
        return try {

            val cookie = request.cookie("Authorization")?.value?.let { UUID.fromString(it) }

            if (cookie != null && playerServices.authenticatePlayer(cookie) != null) {
                return true
            }

            val token = UUID.fromString(
                request.header("Authorization")?.removePrefix("Bearer ") ?: return false,
            )

            playerServices.authenticatePlayer(token) != null

        } catch (e: Exception) {
            false
        }
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

    private fun String.parseURLEncodedString(): String {
        return URLDecoder.decode(this, Charsets.UTF_8)
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
            "outgoing response: status={}, content-type={}, body={}",
            response.status,
            response.header("content-type"),
            response.bodyString(),
        )
    }


}
