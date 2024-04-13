package pt.isel.ls.storage

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.storage.mem.SessionsDataMemGame

/**
 *  Game Data management interface
 *
 *  This interface is used to manage the game data
 *
 *  @property create Create a game in the database
 *  @property getById Read a game from the database
 *  @property getAllGames Read all games from the database
 *  @property update Update a game in the database
 *  @property delete Delete a game from the database
 *  @property isGameNameStored Returns a boolean to verify if a game name already stored on the database
 *  @property getGamesSearch Read all games from the database
 */
interface SessionsDataGame {

    /**
     * Create a game in the database mock
     *
     * This function creates a new game object and adds it to the database mock
     *
     * @param game The [Game] object to be created
     * @return The last identifier
     */
    fun create(game: Game): UInt

    /**
     * Checks for the name of a game in the database mock
     *
     * This function uses the [isGameNameStored] function from the [SessionsDataMemGame] class
     *
     * @param name The game name to be checked
     */
    fun isGameNameStored(name: Name): Boolean

    /**
     * Read games from the database mock
     *
     * The function [getGamesSearch] gets all the games that match the given genres and developer
     *
     * @param genres The game genres
     * @param developer The game developer
     * @return The list of game objects that match the given genres and developer
     */
    fun getGamesSearch(genres: Set<Genre>?, developer: Name?, limit: UInt, skip: UInt): List<Game>

    /**
     * Read all games from the database mock
     *
     * This function gets all the games from the database mock
     *
     * @return The list of game objects
     */
    fun getAllGames(): List<Game>

    /**
     * Read a game from the database mock
     *
     * This function uses the [get] function from the [SessionsDataMemGame] class
     *
     * @param id The game identifier
     * @return The game object with the given id or null if it does not exist
     */
    fun getById(id: UInt): Game?

    /**
     * Update a game in the database mock
     *
     * This function uses the [update] function from the [SessionsDataMemGame] class
     *
     * @param value The game object
     */
    fun update(value: Game): Boolean

    /**
     * Delete a game from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMemGame] class
     *
     * @param id The game identifier
     */
    fun delete(id: UInt): Boolean
}
