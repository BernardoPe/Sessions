package pt.isel.ls.services

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager


/**
 * Service Handler for Game related operations
 *
 * Responsible for handling the business logic of the Game entity
 *
 * @property storage [SessionsDataManager] instance to access the data storage
 *
 */

class GameService(val storage: SessionsDataManager) {

    /**
     * Creates a new game
     *
     * @param name The game name
     * @param developer The game developer
     * @param genres The game genres
     * @return The [GameIdentifier]
     *
     * @throws BadRequestException If the game name already exists in the system
     */
    fun createGame(name: Name, developer: Name, genres: Set<Genre>): GameIdentifier {
        val storageGame = storage.game

        val game = Game(0u, name, developer, genres)

        return storageGame.create(game)
    }

    /**
     * Gets a game by its identifier
     *
     * @param id The game identifier
     * @return The [Game] instance
     *
     * @throws NotFoundException If the game is not found
     */
    fun getGameById(id: UInt): Game {
        return storage.game.getById(id) ?: throw NotFoundException("Game not found")
    }

    /**
     * Searches for games
     *
     * @param genres The game genres
     * @param developer The game developer
     * @param name The game name
     * @param limit The maximum number of games to return
     * @param skip The number of games to skip
     * @return A pair with the list of games and the total number of games matching the search criteria
     */

    fun searchGames(genres: Set<Genre>?, developer: Name?, name: Name?, limit: UInt, skip: UInt): Pair<GameList, Int> {
        return storage.game.getGamesSearch(genres, developer, name, limit, skip)
    }
}

typealias GameIdentifier = UInt

typealias GameList = List<Game>
