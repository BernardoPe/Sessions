package pt.isel.ls.storage

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.player.Token
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import java.util.*

/**
 *  Player Data management interface
 *  The [SessionsDataGame] interface is used to manage the player data
 *
 *  @property create Create a player in the database
 *  @property getById Read a player from the database
 *  @property getAll Get all players from the database
 *  @property update Update a player in the database
 *  @property delete Delete a player from the database
 */
interface SessionsDataPlayer {

    /**
     * Create a player in the database
     *
     *
     * @param player The [Player] object to be created
     * @return A pair with the last identifier and a new UUID
     */
    fun create(player: Player): Pair<UInt, Token>

    /**
     * Read a player from the database
     *
     * @param id The player identifier
     * @return The player object with the given id or null if it does not exist
     */
    fun getById(id: UInt): Player?

    /**
     * Search for players in the database
     *
     * @param name The partial name of the player
     * @param limit The maximum number of players to be returned
     * @param skip The number of players to skip
     *
     * @return A pair with a list of players and the total number of players
     */
    fun getPlayersSearch(name: Name?, limit: UInt, skip: UInt): Pair<List<Player>, Int>


    /**
     * Read all players from the database
     *
     * This function uses the [getAll] function from the [SessionsDataMemPlayer] class
     *
     * @return A list with all the players in the database
     */
    fun getAll(): List<Player>

    /**
     * Update a player in the database
     *
     *
     * @param id The player identifier
     * @param value The new player object
     */
    fun update(id: UInt, value: Player): Boolean

    /**
     * Delete a player from the database
     *
     *
     * @param id The player identifier
     */
    fun delete(id: UInt): Boolean

    /**
     * Get a player and its token object
     *
     * @param token The player token identifier
     * @return A pair with the player and its token objects
     */
    fun getPlayerAndToken(token: UUID): Pair<Player, Token>?

    /**
     * Login a player
     *
     * @param name The player name
     * @param password The player password
     * @return A pair with the player identifier and a new UUID
     */
    fun login(id: UInt): Pair<UInt, Token>

    /**
     * Checks if the player's email is stored in the database
     */
    fun isEmailStored(email: Email): Boolean

    /**
     * Checks if the player's name is stored in the database
     */
    fun isNameStored(name: Name): Boolean


    fun revokeToken(token: UUID): Boolean
}
