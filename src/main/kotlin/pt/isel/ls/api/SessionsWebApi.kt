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
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Status.Companion.UNAUTHORIZED
import org.http4k.routing.path
import pt.isel.ls.dto.*
import pt.isel.ls.exceptions.api.*
import pt.isel.ls.exceptions.services.*
import pt.isel.ls.services.GameService
import pt.isel.ls.services.PlayerService
import pt.isel.ls.services.SessionsService
import pt.isel.ls.utils.Failure
import pt.isel.ls.utils.Success


/**
 * The [SessionsApi] class is responsible for processing HTTP requests and returning the responses.
 *
 * Requests are handled using the processRequest method, which receives a request and an [Operation].
 *
 * @param playerServices The [PlayerService] instance
 * @param gameServices The [GameService] instance
 * @param sessionServices The [SessionsService] instance
 * @property processRequest The method that processes the request and returns the response
 *
 *
 */

class SessionsApi(
    val playerServices: PlayerService,
    val gameServices: GameService,
    val sessionServices: SessionsService
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
        return when (val res = playerServices.createPlayer(player.name, player.email)) {
            is Success -> Response(CREATED)
                .header("content-type", "application/json")
                .body(Json.encodeToString(PlayerCreationOutputModel(res.value.first, res.value.second.toString())))

            is Failure -> when (res.value) {
                PlayerCreationException.UnsafeEmail -> Response(BAD_REQUEST)
                PlayerCreationException.EmailAlreadyExists -> Response(BAD_REQUEST)
            }
        }
    }

    private fun getPlayerDetails(request: Request): Response {
        val pid = request.path("pid")?.toIntOrNull() ?: throw BadRequestException("Invalid Player Identifier")
        return when (val res = playerServices.getPlayerDetails(pid)) {
            is Success -> return Response(OK)
                .header("content-type", "application/json")
                .body(
                    Json.encodeToString(
                        PlayerInfoOutputModel(
                            res.value.pid,
                            res.value.name,
                            res.value.email
                        )
                    )
                )

            is Failure -> when (res.value) {
                PlayerDetailsException.PlayerNotFound -> Response(NOT_FOUND)
            }
        }
    }

    private fun createGame(request: Request): Response {
        val game = parseJsonBody<GameCreationInputModel>(request)
        return when (val res = gameServices.createGame(game.name, game.developer, game.genres)) {
            is Success -> Response(CREATED)
                .header("content-type", "application/json")
                .body(Json.encodeToString(GameCreationOutputModel(res.value)))

            is Failure -> when (res.value) {
                GameCreationException.GameNameAlreadyExists -> Response(BAD_REQUEST)
            }
        }
    }

    private fun getGameById(request: Request): Response {
        val gid = request.path("gid")?.toIntOrNull() ?: throw BadRequestException("Invalid Game Identifier")
        return when (val res = gameServices.getGameById(gid)) {
            is Success -> Response(OK)
                .header("content-type", "application/json")
                .body(
                    Json.encodeToString(
                        GameInfoOutputModel(
                            res.value.gid,
                            res.value.name,
                            res.value.developer,
                            res.value.genres.toList()
                        )
                    )
                )

            is Failure -> when (res.value) {
                GameDetailsException.GameNotFound -> Response(NOT_FOUND)
            }
        }
    }

    private fun getGameList(request: Request): Response {
        val (limit, skip) = (request.query("limit")?.toInt() ?: 5) to (request.query("skip")?.toInt() ?: 0)
        val gameSearch = parseJsonBody<GameSearchInputModel>(request)
        return when (val res = gameServices.searchGames(gameSearch.genres, gameSearch.developer, limit, skip)) {
            is Success -> Response(OK)
                .header("content-type", "application/json")
                .body(
                    Json.encodeToString(
                        GameSearchOutputModel(
                            res.value.map {
                                GameInfoOutputModel(
                                    it.gid,
                                    it.name,
                                    it.developer,
                                    it.genres.toList()
                                )
                            }
                        )
                    )
                )

            is Failure -> when (res.value) {
                GameSearchException.GenresNotFound -> Response(NOT_FOUND)
                GameSearchException.DeveloperNotFound -> Response(NOT_FOUND)
            }
        }
    }

    private fun createSession(request: Request): Response {
        val session = parseJsonBody<SessionCreationInputModel>(request)
        return when (val res = sessionServices.createSession(session.capacity, session.gid, session.date)) {
            is Success -> Response(CREATED)
                .header("content-type", "application/json")
                .body(Json.encodeToString(SessionCreationOutputModel(res.value)))

            is Failure -> when (res.value) {
                SessionCreationException.InvalidCapacity -> Response(BAD_REQUEST)
                SessionCreationException.InvalidDate -> Response(BAD_REQUEST)
                SessionCreationException.GameNotFound -> Response(NOT_FOUND)
            }
        }
    }

    private fun addPlayerToSession(request: Request): Response {
        val sid = request.path("sid")?.toIntOrNull() ?: throw BadRequestException("Invalid Session Identifier")
        val pid = request.path("pid")?.toIntOrNull() ?: throw BadRequestException("Invalid Player Identifier")
        return when (val res = sessionServices.addPlayer(sid, pid)) {
            is Success -> Response(OK)
                .header("content-type", "application/json")
                .body(Json.encodeToString(SessionAddPlayerOutputModel(res.value)))

            is Failure -> when (res.value) {
                SessionAddPlayerException.SessionNotFound -> Response(NOT_FOUND)
                SessionAddPlayerException.PlayerNotFound -> Response(NOT_FOUND)
                SessionAddPlayerException.InvalidCapacity -> Response(BAD_REQUEST)
            }
        }
    }

    private fun getSessionById(request: Request): Response {
        val sid = request.path("sid")?.toIntOrNull() ?: throw BadRequestException("Invalid Session Identifier")
        return when (val res = sessionServices.getSessionById(sid)) {
            is Success -> Response(OK)
                .header("content-type", "application/json")
                .body(
                    Json.encodeToString(
                        SessionInfoOutputModel(
                            res.value.sid,
                            res.value.capacity,
                            res.value.date,
                            GameInfoOutputModel(
                                res.value.gameSession.gid,
                                res.value.gameSession.name,
                                res.value.gameSession.developer,
                                res.value.gameSession.genres.toList()
                            ),
                            res.value.playersSession.map {
                                PlayerInfoOutputModel(
                                    it.pid,
                                    it.name,
                                    it.email
                                )
                            }
                        )
                    )
                )

            is Failure -> when (res.value) {
                SessionDetailsException.SessionNotFound -> Response(NOT_FOUND)
            }
        }

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
        return when (res) {
            is Success -> Response(OK)
                .header("content-type", "application/json")
                .body(
                    Json.encodeToString(
                        SessionSearchOutputModel(
                            res.value.map { session ->
                                SessionInfoOutputModel(
                                    session.sid,
                                    session.capacity,
                                    session.date,
                                    GameInfoOutputModel(
                                        session.gameSession.gid,
                                        session.gameSession.name,
                                        session.gameSession.developer,
                                        session.gameSession.genres.toList()
                                    ),
                                    session.playersSession.map {
                                        PlayerInfoOutputModel(
                                            it.pid,
                                            it.name,
                                            it.email
                                        )
                                    }
                                )
                            }
                        )
                    )
                )

            is Failure -> when (res.value) {
                SessionSearchException.GameNotFound -> Response(Status.NOT_FOUND)
                SessionSearchException.PLayerNotFound -> Response(Status.NOT_FOUND)
                SessionSearchException.InvalidDate -> Response(BAD_REQUEST)
            }
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
    fun processRequest(request: Request, service: Operation): Response {

        val (processor, requiresAuth) = processors[service] ?: return Response(INTERNAL_SERVER_ERROR)

        if (request.bodyString().isNotBlank() && request.header("content-type") != "application/json")
            return Response(BAD_REQUEST).header("content-type", "application/json").body(
                Json.encodeToString(
                    UnsupportedMediaTypeException()
                )
            )

        if (requiresAuth && !verifyAuth(request))
            return Response(UNAUTHORIZED).header("content-type", "application/json").body(
                Json.encodeToString(
                    UnauthorizedException()
                )
            )

        return try {
            processor(request)
        } catch (e: WebExceptions) {
            Response(Status(e.status, e.description)).header("content-type", "application/json").body(Json.encodeToString(e))
        }
        catch (e: Exception) {
            Response(INTERNAL_SERVER_ERROR).header("content-type", "application/json").body(
                Json.encodeToString(
                    InternalServerErrorException()
                )
            )
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
