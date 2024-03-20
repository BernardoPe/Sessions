package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.dto.SessionAddPlayerOutputModel
import pt.isel.ls.dto.SessionCreationOutputModel
import pt.isel.ls.dto.SessionInfoOutputModel
import pt.isel.ls.dto.SessionSearchOutputModel
import pt.isel.ls.exceptions.services.*

/**
 * Converts a [SessionIdentifier] to a [SessionCreationOutputModel]
 * @return The session creation DTO
 */
fun SessionIdentifier.toSessionCreationDTO() = SessionCreationOutputModel(this)

/**
 * Converts a [SessionAddPlayerResult] to a [SessionAddPlayerOutputModel]
 * @return The session add player DTO
 */

fun SessionAddPlayerMessage.toSessionAddPlayerDTO() = SessionAddPlayerOutputModel(this)

/**
 * Converts a [Session] to [SessionInfoOutputModel]
 * @return The session info DTO
 */
fun Session.toSessionInfoDTO() = SessionInfoOutputModel(id, capacity, date.toString(), gameSession.toGameInfoDTO(), playersSession.map { it.toPlayerInfoDTO() })

/**
 * Converts [SessionSearchResult] to [SessionSearchOutputModel]
 * @return The session search DTO
 */
fun SessionList.toSessionSearchDTO() = SessionSearchOutputModel(map { it.toSessionInfoDTO() })