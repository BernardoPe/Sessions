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
     * @param value The game object to be created
     */
    override fun create(value: Game) {
        // Add the game object to the database mock
        super.create(value)
    }

    /**
     * Read a game from the database mock
     *
     * This function uses the [get] function from the [SessionsDataMem] class
     *
     * @param id The game identifier
     * @return The game object with the given id or null if it does not exist
     */
    override fun get(id: Int): Game? {
        return super.get(id)
    }

    /**
     * Update a game in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMem] class
     *
     * @param id The game identifier
     * @param game The new game object
     */
    override fun update(id: Int, value: Game) {
        super.update(id, value)
    }

    /**
     * Delete a game from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMem] class
     *
     * @param id The game identifier
     */
    override fun delete(id: Int) {
        super.delete(id)
    }
}