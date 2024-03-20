package pt.isel.ls.storage

import pt.isel.ls.data.domain.player.Player
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
     * @param name The player name to be created
     * @param email The player email to be created
     * @return The player identifier and the player UUID
     */
    fun create(name: String, email: String): Pair<Int, UUID>

    /**
     * Read a player from the database
     *
     * @param id The player identifier
     * @return The player object with the given id or null if it does not exist
     */
    fun getById(id: Int): Player?

    /**
     * Returns a boolean to verify is an email already stored on the database
     *
     * @param email The player identifier
     * @return Boolean to verify the existence of the player's email
     */

    fun isEmailStored(email: String): Boolean

    /**
     * Get all players from the database
     *
     * @return A list with all the players in the database
     */
    fun getAll(): List<Player>

    /**
     * Update a player in the database
     *
     * @param id The player identifier
     * @param value The new player object
     */
    fun update(id: Int, value: Player)

    /**
     * Delete a player from the database
     *
     * @param id The player identifier
     */
    fun delete(id: Int)

}