package pt.isel.ls.services

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager
import java.util.*


/**
 * Service Handler for Player related operations
 *
 * Responsible for handling the business logic of the Player entity
 *
 * @property dataManager [SessionsDataManager] instance to access the data storage
 *
 */
class PlayerService(private val dataManager: SessionsDataManager) {

    private val playerStorage = dataManager.player

    /**
     * Creates a new player
     *
     * @param name The player name
     * @param email The player email
     * @return The [PlayerCredentials]
     *
     * @throws BadRequestException If the player name or email already exists in the system
     */
    fun createPlayer(name: Name, email: Email): PlayerCredentials {

        return dataManager.executeTransaction {

            if (playerStorage.isEmailStored(email)) {
                throw BadRequestException("Given Player email already exists")
            }

            if (playerStorage.isNameStored(name)) {
                throw BadRequestException("Given Player name already exists")
            }

            val player = Player(0u, name, email,0L)

            playerStorage.create(player)
        }

    }

    /**
     * Authenticates a player
     *
     * @param token The player token
     * @return The [Player] instance
     */
    fun authenticatePlayer(token: UUID): Player? {
        return playerStorage.getByToken(token)
    }

    /**
     * Gets a player by its identifier
     *
     * @param pid The player identifier
     * @return The [Player] instance
     *
     * @throws NotFoundException If the player is not found
     */
    fun getPlayerDetails(pid: UInt): Player {
        return playerStorage.getById(pid) ?: throw NotFoundException("Player not found")
    }

    /**
     * Searches for players
     *
     * @param name The player name
     * @param limit The maximum number of players to return
     * @param skip The number of players to skip
     * @return The [PlayerList] and the total number of players
     */
    fun getPlayerList(name: Name?, limit: UInt, skip: UInt): Pair<PlayerList, Int> {
        return dataManager.player.getPlayersSearch(name, limit, skip)
    }

}

typealias PlayerList = List<Player>

typealias PlayerCredentials = Pair<UInt, UUID>
