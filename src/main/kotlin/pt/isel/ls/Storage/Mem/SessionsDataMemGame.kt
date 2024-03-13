package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Game.Game
import pt.isel.ls.Storage.SessionsDataGame

/**
 *  SessionsDataMemGame
 *
 *  Game Data management class
 *
 *  Uses the [SessionsDataGameMem] class to manage the game data
 *
 *  In this case the [getter] is a lambda function used as a comparator
 *
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
     * Create a game in the database mock
     *
     * This function uses the [create] function from the [SessionsDataGameMem] class
     *
     * @param value The game object to be created
     */
    override fun create(value: Game) {
        // Add the game object to the database mock
        db.add(value)
    }

    /**
     * Read a game from the database mock
     *
     * This function uses the [get] function from the [SessionsDataGameMem] class
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
     * This function uses the [getAll] function from the [SessionsDataGameMem] class
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
     * This function uses the [update] function from the [SessionsDataGameMem] class
     *
     * @param id The game identifier
     * @param value The new game object
     */
    override fun update(id: Int, value: Game) {
        // Update the game object in the database mock
        db.forEach {
            // search for the game with the given id
            if (it.gid == id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                // add the new game to the database mock
                db.add(value)
            }
        }
    }

    /**
     * Delete a game from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataGameMem] class
     *
     * @param id The game identifier
     */
    override fun delete(id: Int) {
        // Delete the game object from the database mock
        db.forEach {
            // search for the game with the given id
            if (it.gid == id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
            }
        }
    }
}