package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Game.Game
import pt.isel.ls.Storage.SessionsDataGame

/**
 *  SessionsDataMemGame
 *
 *  Game Data management class
 *
 *  Uses the [SessionsDataMemGame] class to manage the game data
 */

class SessionsDataMemGame : SessionsDataGame {

    /**
     * Database Mock
     *
     * This is a mockup of the database, used for testing purposes.
     *
     * @property db The database.
     */
    var db: MutableList<Game> = mutableListOf()

    /**
     * Last Identifier
     *
     * The last identifier is used to keep track of the last identifier used in the database mock
     * When a new game instance is added to the database mock, the last identifier is incremented
     *
     * @property lastId The last identifier.
     */
    var lastId = 0

    /**
     * Create a game in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMemGame] class
     *
     * @param value The game object to be created
     */
    override fun create(value: Game) {
        // Add the game object to the database mock
        // Start by incrementing the last identifier
        lastId++
        // Add the updated game object to the database mock
        db.add(Game(
            lastId,
            value.name,
            value.developer,
            value.genres
        ))
    }

    /**
     * Read a game from the database mock
     *
     * This function uses the [get] function from the [SessionsDataMemGame] class
     *
     * @param id The game identifier
     * @return The game object with the given id or null if it does not exist
     */
    override fun getById(id: Int): Game? {
        // Read the game object from the database mock
        db.forEach {
            // search for the game with the given id
            if (it.gid == id) {
                // if found
                // return the game object
                return it
            }
        }
        return null
    }

    /**
     * Read all games from the database mock
     *
     * This function uses the [getAll] function from the [SessionsDataMemGame] class
     *
     * @return A list with all the games in the database
     */
    override fun getAll(): List<Game> {
        // Read all the game objects from the database mock
        return db
    }

    /**
     * Update a game in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMemGame] class
     *
     * @param id The game identifier
     * @param value The new game object
     */
    override fun update(id: Int, value: Game): Boolean {
        // Update the game object in the database mock
        db.forEach {
            // search for the game with the given id
            if (it.gid == id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                // add the new game to the database mock
                db.add(value)
                // alert that the game was updated
                return true
            }
        }
        // alert otherwise
        return false
    }

    /**
     * Delete a game from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMemGame] class
     *
     * @param id The game identifier
     */
    override fun delete(id: Int): Boolean {
        // Delete the game object from the database mock
        db.forEach {
            // search for the game with the given id
            if (it.gid == id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                // alert that the game was deleted
                return true
            }
        }
        // alert otherwise
        return false
    }
}