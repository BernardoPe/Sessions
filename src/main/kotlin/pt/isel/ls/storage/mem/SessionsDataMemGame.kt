package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.storage.SessionsDataGame

/**
 *  SessionsDataMemGame
 *
 *  Game Data management class
 *
 *  Uses the [SessionsDataMemGame] class to manage the game data
 */

class SessionsDataMemGame : SessionsDataGame, MemManager() {

    override fun create(game: Game): UInt {
        // Check if the game name already exists in the database mock
        if (gameDB.any { it.name == game.name }) {
            throw BadRequestException("Game name already exists")
        }
        // Add the game object to the database mock
        gameDB.add(
            Game(
                gid,
                game.name,
                game.developer,
                game.genres,
            ),
        )
        // Return the last identifier
        return gid - 1u
    }

    override fun getById(id: UInt): Game? {
        // Read the game object from the database mock
        gameDB.forEach {
            // search for the game with the given id
            if (it.id == id) {
                // if found
                // return the game object
                return it
            }
        }
        return null
    }

    override fun getGamesSearch(
        genres: Set<Genre>?,
        developer: Name?,
        name: Name?,
        limit: UInt,
        skip: UInt
    ): Pair<List<Game>, Int> {
        // Read all the game objects from the database mock that match the given genres and developer
        // Start by checking the genres
        var games = gameDB.filter { game ->
            // check if the game genres match the given genres
            genres?.all { game.genres.contains(it) } ?: true
        }

        games = games.filter { game ->
            // check if the game name matches the given name
            name?.let { game.name.toString().lowercase().startsWith(name.toString(), ignoreCase = true) } ?: true
        }

        // Then check the developer
        games = games.filter { game ->
            // check if the game developer matches the given developer
            developer?.let { game.developer == it } ?: true
        }

        games = games.sortedBy { it.id }

        return Pair(games.drop(skip.toInt()).take(limit.toInt()), games.size)

    }

    override fun getAllGames(): List<Game> {
        return gameDB
    }

    override fun update(value: Game): Boolean {
        // Uses the SessionsDataMemGame class to manage the game object in the database mock
        gameDB.forEachIndexed { index, it ->
            // search for the game with the given id
            if (it.id == value.id) {
                // if found
                // remove the game from the database mock
                gameDB.removeAt(index)
                // add the new game to the database mock
                gameDB.add(value)
                return true
            }
        }
        // alert if the game was not found
        return false
    }

    override fun delete(id: UInt): Boolean {
        // Delete the game object from the database mock
        gameDB.forEachIndexed { index, it ->
            // search for the game with the given id
            if (it.id == id) {
                // if found
                // remove the game from the database mock
                gameDB.removeAt(index)
                return true
            }
        }
        return false
    }

    override fun clear() {
        gameDB.clear()
    }

}
