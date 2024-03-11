package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Game.Game


// Getter function
private val getter: (Game, Int) -> Boolean = { game, id -> game.gid == id }

/**
 *  SessionsDataMemGame
 *
 *  Game Data management class
 *
 *  Uses the [SessionsDataMem] class to manage the game data
 *
 *  In this case the [getter] is a lambda function used as a comparator
 *
 */

class SessionsDataMemGame : SessionsDataMem<Game>(getter) {

    /**
     * Create a game in the database mock
     *
     * This function uses the [create] function from the [SessionsDataMem] class
     *
     * @param game The game object to be created
     */
    fun createGame(game: Game) {
        // Add the game object to the database mock
        create(game)
    }

    /**
     * Read a game from the database mock
     *
     * This function uses the [read] function from the [SessionsDataMem] class
     *
     * @param id The game identifier
     * @return The game object with the given id or null if it does not exist
     */
    fun readGame(id: Int): Game? {
        return read(id)
    }

    /**
     * Update a game in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMem] class
     *
     * @param id The game identifier
     * @param game The new game object
     */
    fun updateGame(id: Int, game: Game) {
        // Update the game in the database mock with the new game object
        update(id, game)
    }

    /**
     * Delete a game from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMem] class
     *
     * @param id The game identifier
     */
    fun deleteGame(id: Int) {
        // Delete the game from the database mock with the given id
        delete(id)
    }
}