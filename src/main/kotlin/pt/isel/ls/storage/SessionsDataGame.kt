package pt.isel.ls.storage

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.data.domain.Name
import pt.isel.ls.exceptions.*

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
 *  @property isGenresStored Returns a boolean to verify if a list of genres of an game are already stored on the database
 *  @property isDeveloperStored Returns a boolean to verify if the name of a developer already stored on the database
 *  @property getGamesSearch Read all games from the database
 */
interface SessionsDataGame {

    /**
     * Create a game in the database mock
     *
     * This function creates a new game object and adds it to the database mock
     *
     * @param name The game name to be created for the game
     * @param developer The name of the developer to be created for the game
     * @param genres The list of genres to be created for the game
     * @return The last identifier
     * @throws BadRequestException If the game name already exists
     */
    fun create(name: Name, developer: Name, genres: Set<Genre>): UInt

    /**
     * Checks for the name of a game in the database mock
     *
     * This function uses the [isGameNameStored] function from the [SessionsDataMemGame] class
     *
     * @param name The game name to be checked
     */
    // This may or may not be a necessary method
    fun isGameNameStored(name: Name): Boolean

    /**
     * Checks a list of genres in the database mock
     *
     * This function uses the [isGenresStored] function from the [SessionsDataMemGame] class
     *
     * @param genres The game name to be checked
     */
    // This may or may not be a necessary method
    fun isGenresStored(genres: Set<Genre>): Boolean

    /**
     * Checks for the name of the developer in the database mock
     *
     * This function uses the [isDeveloperStored] function from the [SessionsDataMemGame] class
     *
     * @param developer The name of the developer to be checked
     */
    // This may or may not be a necessary method
    fun isDeveloperStored(developer: Name): Boolean

    /**
     * Read games from the database mock
     *
     * The function [getGamesSearch] gets all the games that match the given genres and developer
     *
     * @param genres The game genres
     * @param developer The game developer
     * @return The list of game objects that match the given genres and developer
     */
    fun getGamesSearch(genres: Set<Genre>, developer: Name, limit: UInt, skip: UInt): List<Game>

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
     * @param id The game identifier
     * @param value The new game object
     * @throws NotFoundException If the game with the given id does not exist
     */
    fun update(value: Game)

    /**
     * Delete a game from the database mock
     *
     * This function uses the [delete] function from the [SessionsDataMemGame] class
     *
     * @param id The game identifier
     * @throws NotFoundException If the game with the given id does not exist
     */
    fun delete(id: UInt)
}