package pt.isel.ls.services

import org.mindrot.jbcrypt.BCrypt
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.player.Token
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.data.domain.primitives.Password
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager
import java.util.*

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
        val hashedPassword = BCrypt.hashpw(password.toString(), BCrypt.gensalt(12))

        val player = Player(0u, name, email, PasswordHash(hashedPassword))

        return storagePlayer.create(player)
    }

    /**
     * Logs in a player
     *
     * @param name The player name
     * @param password The player password
     * @return The [PlayerCredentials]
     *
     * @throws NotFoundException If the player is not found
     * @throws BadRequestException If the player password is incorrect
     */
    fun loginPlayer(name: Name, password: Password): PlayerCredentials {

        val playerStorage = storage.player

        if (name.toString() == "") {
            throw BadRequestException("Name must be provided")
        }

        val player = playerStorage.getPlayersSearch(name = name, 1u, 0u).first.first()

        if (player.name != name) {
            throw NotFoundException("Given player name not Found")
        }

        /* This line of code uses the JBCrypt library to match the password
            and the hashed password that was stored on the database */
        if (BCrypt.checkpw(password.toString(), player.password.toString())) {
            throw BadRequestException("Given Player password is incorrect")
        }

        return playerStorage.login(player.id)
    }

    /**
     * Logs out a player
     *
     * @param token The player token
     * @return True if the player was successfully logged out, false otherwise
     */
    fun logoutPlayer(token: UUID): Boolean {
        return storage.player.revokeToken(token)
    }

    /**
     * Renovates a player token
     *
     * @param token The player token
     * @return The new [Token] object
     *
     * @throws NotFoundException If the token is not found
     */
    fun renovateToken(token: UUID): Token {
        // Retrieve the token from the database
        val existingToken = storage.player.getToken(token)

        // Check if the token is expired
        if (existingToken?.isExpired() == true) {
            // Delete the expired token from the database
            storage.player.revokeToken(token)

            // Create a new token for the same player
            val newToken = storage.player.login(existingToken.playerId)

            // Return the new token
            return newToken.second
        }

        // If the token is not expired, return the existing token
        return existingToken ?: throw NotFoundException("Token not found")
    }

    /**
     * Authenticates a player
     *
     * @param token The player token
     * @return The [Player] instance
     */
    fun authenticatePlayer(token: UUID): Player? {
        return storage.player.getPlayerByToken(token)
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

typealias PlayerCredentials = Pair<UInt, Token>
