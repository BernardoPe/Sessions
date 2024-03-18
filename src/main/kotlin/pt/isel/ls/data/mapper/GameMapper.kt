package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.dto.GameCreationOutputModel
import pt.isel.ls.dto.GameInfoOutputModel
import pt.isel.ls.dto.GameSearchOutputModel
import pt.isel.ls.services.GameIdentifier
import pt.isel.ls.services.GameSearchResult


/**
 * Converts a [Game] to [GameInfoOutputModel]
 * @return The game info DTO
 */
fun Game.toGameInfoDTO() = GameInfoOutputModel(gid, name, developer, genres.toList())


/**
 * Converts [GameSearchResult] to [GameSearchOutputModel]
 * @return The game search DTO
 */

fun GameSearchResult.toGameSearchDTO() = GameSearchOutputModel(map { it.toGameInfoDTO() })


/**
 * Converts a [GameIdentifier] to a [GameCreationOutputModel]
 * @return The game creation DTO
 */
fun GameIdentifier.toGameCreationDTO() = GameCreationOutputModel(this)

