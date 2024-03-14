package pt.isel.ls.Storage

import pt.isel.ls.DTO.Game.Game

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
     * @param value The game object to be created
     */
    fun create(value: Game)

    /**
     * Read all games from the database
     *
     * @return A list with all the games in the database
     */
    fun getAll(): List<Game>

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
    fun update(id: Int, value: Game): Boolean

    /**
     * Delete a game from the database
     *
     * @param id The game identifier
     */
    fun delete(id: Int): Boolean
}