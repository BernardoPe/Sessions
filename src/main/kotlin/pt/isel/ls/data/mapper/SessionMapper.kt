package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.dto.SessionAddPlayerOutputModel
import pt.isel.ls.dto.SessionCreationOutputModel
import pt.isel.ls.dto.SessionInfoOutputModel
import pt.isel.ls.dto.SessionSearchOutputModel
import pt.isel.ls.services.SessionAddPlayerResult
import pt.isel.ls.services.SessionIdentifier
import pt.isel.ls.services.SessionSearchResult


/**
 * Converts a [SessionIdentifier] to a [SessionCreationOutputModel]
 * @return The session creation DTO
 */
fun SessionIdentifier.toSessionCreationDTO() = SessionCreationOutputModel(this)

/**
 * Converts a [SessionAddPlayerResult] to a [SessionAddPlayerOutputModel]
 * @return The session add player DTO
 */

fun SessionAddPlayerResult.toSessionAddPlayerDTO() = SessionAddPlayerOutputModel(this)

/**
 * Converts a [Session] to [SessionInfoOutputModel]
 * @return The session info DTO
 */
fun Session.toSessionInfoDTO() = SessionInfoOutputModel(sid, capacity, date, gameSession.toGameInfoDTO(), playersSession.map { it.toPlayerInfoDTO() })

/**
 * Converts [SessionSearchResult] to [SessionSearchOutputModel]
 * @return The session search DTO
 */
fun SessionSearchResult.toSessionSearchDTO() = SessionSearchOutputModel(map { it.toSessionInfoDTO() })