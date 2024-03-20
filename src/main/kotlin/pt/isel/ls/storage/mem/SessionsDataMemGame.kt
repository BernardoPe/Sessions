package pt.isel.ls.storage.mem

import pt.isel.ls.domain.game.Game
import pt.isel.ls.exceptions.GameNameAlreadyExistsException
import pt.isel.ls.exceptions.GameNotFoundException
import pt.isel.ls.storage.SessionsDataGame

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
    private var db: MutableList<Game> = mutableListOf()

    /**
     * Last Identifier
     *
     * The last identifier is used to keep track of the last identifier used in the database mock
     * When a new game instance is added to the database mock, the last identifier is incremented
     *
     * @property lastId The last identifier.
     */
    private var lastId = 0

    /**
     * Create a game in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMemGame] class
     *
     * @param value The game object to be created
     */
    override fun create(name: String, developer: String, genres: Set<String>): Int {
        // Add the game object to the database mock
        // Start by checking if the game name already exists
        db.any { it.name == name }.let {
            if (it) throw GameNameAlreadyExistsException("Provided game name already exists.")
        }
        // Add the updated game object to the database mock
        db.add(
            Game(
                lastId++,
                name,
                developer,
                genres
            )
        )
        // Return the last identifier
        return lastId
    }

    /**
     * Checks for the name of a game in the database mock
     *
     * This function uses the [isGameNameStored] function from the [SessionsDataMemGame] class
     *
     * @param name The game name to be checked
     */

    override fun isGameNameStored(name: String): Boolean {
        // Check if the game name already exists in the database mock
        return db.any { it.name == name }
    }

    /**
     * Checks a list of genres in the database mock
     *
     * This function uses the [isGenresStored] function from the [SessionsDataMemGame] class
     *
     * @param genres The game name to be checked
     */

    override fun isGenresStored(genres: Set<String>): Boolean {
        // Check if the list of genres already exists in the database mock
        return db.any { it.genres == genres }
    }

    /**
     * Checks for the name of the developer in the database mock
     *
     * This function uses the [isDeveloperStored] function from the [SessionsDataMemGame] class
     *
     * @param developer The name of the developer to be checked
     */

    override fun isDeveloperStored(developer: String): Boolean {
        // Check if the developer name already exists in the database mock
        return db.any { it.developer == developer }
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
    override fun getGamesSearch(genres: Set<String>, developer: String, limit: Int, skip: Int): List<Game> {
        // Read all the game objects from the database mock that match the given genres and developer
        // Start by checking the genres
        val games = db.filter { it.genres.containsAll(genres) }
        // Then check the developer
        return games.filter { it.developer == developer }
    }

    /**
     * Update a game in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMemGame] class
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
        // alert if the game was not found
        throw GameNotFoundException("Game with the given id does not exist")
    }

    /**
     * Delete a game from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMemGame] class
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
        // alert if the game was not found
        throw GameNotFoundException("Game with the given id does not exist")
    }
}