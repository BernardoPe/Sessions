package pt.isel.ls.storage

import pt.isel.ls.data.domain.player.Player

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
     * @param value The player object to be created
     */
    fun create(value: Player)

    /**
     * Read a player from the database
     *
     * @param id The player identifier
     * @return The player object with the given id or null if it does not exist
     */
    fun getById(id: Int): Player?

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
    fun update(id: Int, value: Player): Boolean

    /**
     * Delete a player from the database
     *
     * @param id The player identifier
     */
    fun delete(id: Int): Boolean

}