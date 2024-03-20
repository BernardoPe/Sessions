package pt.isel.ls.storage

import pt.isel.ls.data.domain.game.Game

/**
 *  Game Data management interface
 *
 *  This interface is used to manage the game data
 *
 *  @property create Create a game in the database
 *  @property getById Read a game from the database
 *  @property getAll Read all games from the database
 *  @property update Update a game in the database
 *  @property delete Delete a game from the database
 */
interface SessionsDataGame {

    /**
     * Create a game in the database
     *
     * @param name The game name to be created for the game
     * @param developer The name of the developer to be created for the game
     * @param genres The list of genres to be created for the game
     */
    fun create(name: String, developer: String, genres: Set<String>): Int

    /**
     * Returns a boolean to verify if a game name already stored on the database
     * @param email Email of the player
     * @return Boolean to verify the existence of the name of a game on the database
     */

    // This may or may not be a necessary method
    fun isGameNameStored(name: String): Boolean

    /**
     * Returns a boolean to verify if a list of genres of an game are already stored on the database
     * @param genres List of genres
     * @return Boolean to verify the existence of list of genres on the database
     */

    // This may or may not be a necessary method
    fun isGenresStored(genres: Set<String>): Boolean

    /**
     * Returns a boolean to verify if the name of a developer already stored on the database
     * @param developer Name of the developer
     * @return Boolean to verify the name of a developer stored on the datebase
     */

    // This may or may not be a necessary method
    fun isDeveloperStored(developer: String): Boolean

    /**
     * Read all games from the database
     *
     * @return A list with all the games in the database
     */

    fun getGamesSearch(genres: Set<String>, developer: String, limit: Int, skip: Int): List<Game>

    /**
     * Read a game from the database
     *
     * @param id The game identifier
     * @return The game object with the given id or null if it does not exist
     */
    fun getById(id: Int): Game?

    /**
     * Update a game in the database
     *
     * @param id The game identifier
     * @param value The new game object
     */
    fun update(id: Int, value: Game)

    /**
     * Delete a game from the database
     *
     * @param id The game identifier
     */
    fun delete(id: Int)
}