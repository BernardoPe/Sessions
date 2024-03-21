package pt.isel.ls.storage

import pt.isel.ls.data.domain.Email
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.player.Player
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
     * Create a player in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMemPlayer] class
     *
     * @param name The player name to be created
     * @param email The player email to be created
     * @return A pair with the last identifier and a new UUID
     */
    fun create(name: Name, email: Email): Pair<UInt, UUID>

    /**
     * Read a player from the database mock
     *
     * This function uses the [get] function from the [SessionsDataMemPlayer] class
     *
     * @param id The player identifier
     * @return The player object with the given id or null if it does not exist
     */
    fun getById(id: UInt): Player?

    /**
     * Checks the player's email on the database mock
     *
     * This function uses the [isEmailStored] function from the [SessionsDataMemPlayer] class
     *
     * @param email The player email to be checked
     * @return A boolean indicating if the player email exists in the database mock
     */
    fun isEmailStored(email: Email): Boolean

    /**
     * Read all players from the database mock
     *
     * This function uses the [getAll] function from the [SessionsDataMemPlayer] class
     *
     * @return A list with all the players in the database
     */
    fun getAll(): List<Player>

    /**
     * Update a player in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMemPlayer] class
     *
     * @param id The player identifier
     * @param value The new player object
     */
    fun update(id: UInt, value: Player) : Boolean

    /**
     * Delete a player from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMemPlayer] class
     *
     * @param id The player identifier
     */
    fun delete(id: UInt) : Boolean

    /**
     * Checks the player's token on the database
     *
     * @param token The player token to be checked
     * @return A boolean indicating if the player token exists in the database mock
     */
    fun getByToken(token: UUID): Player?

}