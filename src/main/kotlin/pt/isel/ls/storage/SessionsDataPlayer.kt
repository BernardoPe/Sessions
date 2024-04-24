package pt.isel.ls.storage

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.util.Email
import pt.isel.ls.data.domain.util.Name
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
    fun create(player: Player): Pair<UInt, UUID>

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
     * Checks the player's email on the database
     *
     *
     * @param email The player email to be checked
     * @return A boolean indicating if the player email exists in the database mock
     */
    fun isEmailStored(email: Email): Boolean

    /**
     * Checks the player's name on the database
     *
     * @param name The player name to be checked
     * @return A boolean indicating if the player name exists in the database mock
     */
    fun isNameStored(name: Name): Boolean

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
     * Checks the player's token on the database
     *
     * @param token The player token to be checked
     * @return A boolean indicating if the player token exists in the database mock
     */
    fun getByToken(token: UUID): Player?
}
