package pt.isel.ls.data.mapper

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.dto.PlayerInfoOutputModel
import pt.isel.ls.services.PlayerCredentials

/**
 * Converts [Player] to [PlayerInfoOutputModel]
 * @return The player info DTO
 */
fun Player.toPlayerInfoDTO() = PlayerInfoOutputModel(id, name.toString(), email.toString())

/**
 * Converts [PlayerCredentials] to [PlayerInfoOutputModel]
 * @return The player creation DTO
 */
fun PlayerCredentials.toPlayerCreationDTO() = pt.isel.ls.dto.PlayerCreationOutputModel(first, second.toString())
