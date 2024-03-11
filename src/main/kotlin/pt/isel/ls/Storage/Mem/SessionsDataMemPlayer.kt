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
     * @param player The player object to be created
     */
    fun createPlayer(player: Player) {
        // Add the player object to the database mock
        create(player)
    }

    /**
     * Read a player from the database mock
     *
     * This function uses the [read] function from the [SessionsDataMem] class
     *
     * @param id The player identifier
     * @return The player object with the given id or null if it does not exist
     */
    fun readPlayer(id: Int): Player? {
        return read(id)
    }

    /**
     * Update a player in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMem] class
     *
     * @param id The player identifier
     * @param player The new player object
     */
    fun updatePlayer(id: Int, player: Player) {
        // Update the player in the database mock with the new player object
        update(id, player)
    }

    /**
     * Delete a player from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMem] class
     *
     * @param id The player identifier
     */
    fun deletePlayer(id: Int) {
        // Delete the player from the database mock with the given id
        delete(id)
    }
}