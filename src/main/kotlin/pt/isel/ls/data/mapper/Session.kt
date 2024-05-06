package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.dto.SessionCreationOutputModel
import pt.isel.ls.dto.SessionInfoOutputModel
import pt.isel.ls.dto.SessionSearchResultOutputModel
import pt.isel.ls.services.SessionIdentifier
import pt.isel.ls.services.SessionList

/**
 * Converts a [SessionIdentifier] to a [SessionCreationOutputModel]
 * @return The session creation DTO
 */
fun SessionIdentifier.toSessionCreationDTO() = SessionCreationOutputModel(this)



/**
 * Converts a [Session] to [SessionInfoOutputModel]
 * @return The session info DTO
 */
fun Session.toSessionInfoDTO() = SessionInfoOutputModel(id, capacity, date.toString(), gameSession.toGameInfoDTO(), playersSession.map { it.toPlayerInfoDTO() })

/**
 * Converts [SessionList] and an [Int] representing total results to [SessionSearchResultOutputModel]
 * @return The session search DTO
 */
fun Pair<SessionList, Int>.toSessionSearchDTO() = SessionSearchResultOutputModel(first.map { it.toSessionInfoDTO() }, second)
