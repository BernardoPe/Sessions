package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Player.Player

// Getter function
private val getter: (Player, Int) -> Boolean = { player, id -> player.pid == id }

/**
 *  SessionsDataMemPlayer
 *
 *  Player Data management class
 *
 *  Uses the [SessionsDataMem] class to manage the player data
 *
 *  In this case the [getter] is a lambda function used as a comparator
 *
 */

class SessionsDataMemPlayer : SessionsDataMem<Player>(getter) {

    /**
     * Create a player in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMem] class
     *
     * @param value The player object to be created
     */
    override fun create(value: Player) {
        super.create(value)
    }

    /**
     * Read a player from the database mock
     *
     * This function uses the [get] function from the [SessionsDataMem] class
     *
     * @param id The player identifier
     * @return The player object with the given id or null if it does not exist
     */
    override fun get(id: Int): Player? {
        return super.get(id)
    }

    /**
     * Update a player in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMem] class
     *
     * @param id The player identifier
     * @param value The new player object
     */
    override fun update(id: Int, value: Player) {
        super.update(id, value)
    }

    /**
     * Delete a player from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMem] class
     *
     * @param id The player identifier
     */
    override fun delete(id: Int) {
        super.delete(id)
    }
}