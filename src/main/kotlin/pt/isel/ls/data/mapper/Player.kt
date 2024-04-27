package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.dto.PlayerCreationOutputModel
import pt.isel.ls.dto.PlayerInfoOutputModel
import pt.isel.ls.dto.PlayerSearchOutputModel
import pt.isel.ls.services.PlayerCredentials
import pt.isel.ls.services.PlayerList

/**
 * Converts [Player] to [PlayerInfoOutputModel]
 * @return The player info DTO
 */
fun Player.toPlayerInfoDTO() = PlayerInfoOutputModel(id, name.toString(), email.toString())

/**
 * Converts [PlayerCredentials] to [PlayerInfoOutputModel]
 * @return The player creation DTO
 */
fun PlayerCredentials.toPlayerCreationDTO() = PlayerCreationOutputModel(first, second.toString())

/**
 * Converts [Pair] of [PlayerList] and [Int] to [PlayerSearchOutputModel]
 * @return The player search DTO
 */
fun Pair<PlayerList, Int>.toPlayerSearchDTO() = PlayerSearchOutputModel(first.map { it.toPlayerInfoDTO() }, second)
