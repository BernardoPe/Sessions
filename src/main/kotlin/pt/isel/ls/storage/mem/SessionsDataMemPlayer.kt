package pt.isel.ls.storage.mem

import pt.isel.ls.domain.player.Player
import pt.isel.ls.storage.SessionsDataPlayer
import java.util.*

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
    private var db: MutableList<Player> = mutableListOf()

    /**
     * Last Identifier
     *
     * The last identifier is used to keep track of the last identifier used in the database mock
     * When a new player instance is added to the database mock, the last identifier is incremented
     *
     * @property lastId The last identifier.
     */
    private var lastId = 0

    /**
     * Create a player in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMemPlayer] class
     *
     * @param value The player object to be created
     */
    override fun create(name: String, email: String): Pair<Int, UUID> {
        // Add the player object to the database mock
        // Start by incrementing the last identifier
        lastId++
        // Add the updated player object to the database mock
        db.add(
            Player(
            lastId,
                name,
                email
            )
        )
        /** This method like the rest of the other database memory methods needs more work */
    }

    /**
     * Read a player from the database mock
     *
     * This function uses the [get] function from the [SessionsDataMemPlayer] class
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
     * This function uses the [getAll] function from the [SessionsDataMemPlayer] class
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
     * This function uses the [update] function from the [SessionsDataMemPlayer] class
     *
     * @param id The player identifier
     * @param value The new player object
     */
    override fun update(id: Int, value: Player): Boolean {
        // Update the player object in the database mock
        db.forEach {
            // search for the player with the given id
            if (it.pid == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
                // add the new player to the database mock
                db.add(value)
                // alert the user that the player was updated
                return true
            }
        }
        // alert otherwise
        return false
    }

    /**
     * Delete a player from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMemPlayer] class
     *
     * @param id The player identifier
     */
    override fun delete(id: Int): Boolean {
        // Delete the player object from the database mock
        db.forEach {
            // search for the player with the given id
            if (it.pid == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
                // alert the user that the player was deleted
                return true
            }
        }
        // alert otherwise
        return false
    }
}