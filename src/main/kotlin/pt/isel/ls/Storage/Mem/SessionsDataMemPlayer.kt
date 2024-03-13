package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Player.Player
import pt.isel.ls.Storage.SessionsDataPlayer

/**
 *  SessionsDataMemPlayer
 *
 *  Player Data management class
 *
 *  Uses the [SessionsDataPlayer] class to manage the player data
 *
 *  In this case the [getter] is a lambda function used as a comparator
 *
 */

class SessionsDataMemPlayer : SessionsDataPlayer {

    /**
     * Database Mock
     *
     * This is a mockup of the database, used for testing purposes.
     *
     * @property db The database.
     */
    var db: MutableList<Player> = mutableListOf()

    /**
     * Create a player in the database mock
     *
     * This function uses the [create] function from the [SessionsDataPlayerMem] class
     *
     * @param value The player object to be created
     */
    override fun create(value: Player) {
        // Add the player object to the database mock
        db.add(value)
    }

    /**
     * Read a player from the database mock
     *
     * This function uses the [get] function from the [SessionsDataPlayerMem] class
     *
     * @param id The player identifier
     * @return The player object with the given id or null if it does not exist
     */
    override fun getById(id: Int): Player? {
        // Read the player object from the database mock
        db.forEach {
            // search for the player with the given id
            if (it.pid == id) {
                // if found
                // return the player object
                return it
            }
        }
        return null
    }

    /**
     * Read all players from the database mock
     *
     * This function uses the [getAll] function from the [SessionsDataPlayerMem] class
     *
     * @return A list with all the players in the database
     */
    override fun getAll(): List<Player> {
        // Read all the player objects from the database mock
        return db
    }

    /**
     * Update a player in the database mock
     *
     * This function uses the [update] function from the [SessionsDataPlayerMem] class
     *
     * @param id The player identifier
     * @param value The new player object
     */
    override fun update(id: Int, value: Player) {
        // Update the player object in the database mock
        db.forEach {
            // search for the player with the given id
            if (it.pid == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
                // add the new player to the database mock
                db.add(value)
            }
        }
    }

    /**
     * Delete a player from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataPlayerMem] class
     *
     * @param id The player identifier
     */
    override fun delete(id: Int) {
        // Delete the player object from the database mock
        db.forEach {
            // search for the player with the given id
            if (it.pid == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
            }
        }
    }
}