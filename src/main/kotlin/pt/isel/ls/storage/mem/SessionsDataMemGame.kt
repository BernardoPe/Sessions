package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.game.Game
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

    override fun isGameNameStored(name: String): Boolean {
        // Check if the game name already exists in the database mock
        return db.any { it.name == name }
    }

    override fun isGenresStored(genres: Set<String>): Boolean {
        // Check if the list of genres already exists in the database mock
        return db.any { it.genres == genres }
    }

    override fun isDeveloperStored(developer: String): Boolean {
        // Check if the developer name already exists in the database mock
        return db.any { it.developer == developer }
    }

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

    override fun getGamesSearch(genres: Set<String>, developer: String, limit: Int, skip: Int): List<Game> {
        // Read all the game objects from the database mock that match the given genres and developer
        // Start by checking the genres
        var games = db.filter { it.genres.containsAll(genres) }
        // Then check the developer
        games = games.filter { it.developer == developer }
        // Check if skip + limit is greater than the size of the list
        val endIndex = if (skip + limit > games.size) games.size else skip + limit
        // Return the sublist
        return games.subList(skip, endIndex)
    }

    override fun getAllGames(): List<Game> {
        // Read all the game objects from the database mock
        return db
    }

    override fun update(id: Int, value: Game) {
        // UpdaUses the SessionsDataMemGame class to managete the game object in the database mock
        db.forEach {
            // search for the game with the given id
            if (it.gid == id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                // add the new game to the database mock
                db.add(value)
                return
            }
        }
        // alert if the game was not found
        throw GameNotFoundException("Game with the given id does not exist")
    }

    override fun delete(id: Int) {
        // Delete the game object from the database mock
        db.forEach {
            // search for the game with the given id
            if (it.gid == id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                return
            }
        }
        // alert if the game was not found
        throw GameNotFoundException("Game with the given id does not exist")
    }
}