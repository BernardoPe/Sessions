package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
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
    private var lastId = 0u

    override fun create(name: Name, developer: Name, genres: Set<Genre>): UInt {
        // Add the game object to the database mock
        // Start by checking if the game name already exists
        db.any { it.name == name }.let {
            if (it) throw BadRequestException("Provided game name already exists.")
        }
        // Add the updated game object to the database mock
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

    override fun isGenresStored(genres: Set<Genre>): Boolean {
        // Check if the list of genres already exists in the database mock
        return db.any { it.genres == genres }
    }

    override fun isDeveloperStored(developer: Name): Boolean {
        // Check if the developer name already exists in the database mock
        return db.any { it.developer == developer }
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

        return filteredGames.drop(skip.toInt()).takeLast(limit.toInt())
    }

    override fun getAllGames(): List<Game> {
        return db
    }

    override fun update(value: Game) {
        // Uses the SessionsDataMemGame class to manage the game object in the database mock
        db.forEach {
            // search for the game with the given id
            if (it.id == value.id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                // add the new game to the database mock
                db.add(value)
                return
            }
        }
        // alert if the game was not found
        throw NotFoundException("Game with the given id does not exist")
    }

    override fun delete(id: UInt) {
        // Delete the game object from the database mock
        db.forEach {
            // search for the game with the given id
            if (it.id == id) {
                // if found
                // remove the game from the database mock
                db.remove(it)
                return
            }
        }
        // alert if the game was not found
        throw NotFoundException("Game with the given id does not exist")
    }
}