package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.dto.GameCreationOutputModel
import pt.isel.ls.dto.GameInfoOutputModel
import pt.isel.ls.dto.GameSearchResultOutputModel
import pt.isel.ls.services.GameIdentifier
import pt.isel.ls.services.GameList

/**
 * Converts a [Game] to [GameInfoOutputModel]
 * @return The game info DTO
 */
fun Game.toGameInfoDTO() = GameInfoOutputModel(id, name.toString(), developer.toString(), genres.map { it.toString() })

/**
 * Converts [GameList] to [GameSearchResultOutputModel]
 * @return The game search DTO
 */

fun GameList.toGameSearchDTO() = map { it.toGameInfoDTO() }

/**
 * Converts a [GameIdentifier] to a [GameCreationOutputModel]
 * @return The game creation DTO
 */
fun GameIdentifier.toGameCreationDTO() = GameCreationOutputModel(this)

/**
 * Converts a [GameList] and an [Int] to a [GameSearchResultOutputModel]
 * @return The game search DTO
 */
fun Pair<GameList, Int>.toGameSearchDTO() = GameSearchResultOutputModel(first.toGameSearchDTO(), second)