package pt.isel.ls.services

import org.mindrot.jbcrypt.BCrypt
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.data.domain.primitives.Password
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager
import java.util.*

/**
 * The salt value for the password hashing
 */
const val SALT = 12

/**
 * Service Handler for Player related operations
 *
 * Responsible for handling the business logic of the Player entity
 *
 * @property storage [SessionsDataManager] instance to access the data storage
 *
 */
class PlayerService(val storage: SessionsDataManager) {

    /**
     * Creates a new player
     *
     * @param name The player name
     * @param email The player email
     * @return The [PlayerCredentials]
     *
     * @throws BadRequestException If the player name or email already exists in the system
     */
    fun createPlayer(name: Name, email: Email, password: Password): PlayerCredentials {
        val storagePlayer = storage.player

        // This is line of code uses the JBCrypt library to hash the password
        val hashedPassword = BCrypt.hashpw(password.toString(), BCrypt.gensalt(SALT))

        val player = Player(0u, name, email, 0L, PasswordHash(hashedPassword))

        return storagePlayer.create(player)
    }

//    fun loginPlayer(name: Name?, email: Email?, password: String): PlayerCredentials {
//        val storagePlayer = storage.player
//
//        if (name == null && email == null) {
//            throw BadRequestException("Name or email must be provided")
//        }
//
//        val player = if (name != null) storagePlayer.getPlayersSearch(name=name, 1u, 0u).first.first()
//        else storagePlayer.getPlayersSearch(email=email!!, 1u, 0u).first.first(
//
//        if (player.name != name) {
//            throw BadRequestException("Given Player name does not match the email")
//        }
//
//        if (player.password != password) {
//            throw BadRequestException("Given Player password does not match the email")
//        }
//
//        return Pair(player.id, player.token)
//    }

    /**
     * Authenticates a player
     *
     * @param token The player token
     * @return The [Player] instance
     */
    fun authenticatePlayer(token: UUID): Player? {
        return storage.player.getByToken(token)
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
        return storage.player.getById(pid) ?: throw NotFoundException("Player not found")
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
        return storage.player.getPlayersSearch(name, limit, skip)
    }

}

typealias PlayerList = List<Player>

typealias PlayerCredentials = Pair<UInt, UUID>
