package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.game.Game
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
    private var lastId = 1u

    override fun create(name: Name, developer: Name, genres: Set<Genre>): UInt {

        db.add(
            Game(
                lastId,
                name,
                developer,
                genres
            )
        )
        // Return the last identifier
        return lastId++
    }

    override fun isGameNameStored(name: Name): Boolean {
        // Check if the game name already exists in the database mock
        return db.any { it.name == name }
    }

    override fun getById(id: UInt): Game? {
        // Read the game object from the database mock
        db.forEach {
            // search for the game with the given id
            if (it.id == id) {
                // if found
                // return the game object
                return it
            }
        }
        return null
    }

    override fun getGamesSearch(genres: Set<Genre>, developer: Name, limit: UInt, skip: UInt): List<Game> {
        // Read all the game objects from the database mock that match the given genres and developer
        // Start by checking the genres
        val games = db.filter { it.genres.containsAll(genres) }
        // Then check the developer
        // Check if skip + limit is greater than the size of the list
        val filteredGames = games.filter { it.developer == developer }

        return filteredGames.sortedBy { it.id }.drop(skip.toInt()).take(limit.toInt())
    }

    override fun getAllGames(): List<Game> {
        return db
    }

    override fun update(value: Game) : Boolean {
        // Uses the SessionsDataMemGame class to manage the game object in the database mock
        db.forEach {
            // search for the game with the given id
            if (it.id == value.id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                // add the new game to the database mock
                db.add(value)
                return true
            }
        }
        // alert if the game was not found
        return false
    }

    override fun delete(id: UInt) : Boolean {
        // Delete the game object from the database mock
        db.forEach {
            // search for the game with the given id
            if (it.id == id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                return true
            }
        }
       return false
    }
}