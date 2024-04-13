package pt.isel.ls.services

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.ConflictException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager

class GameService(val storage: SessionsDataManager) {
    fun createGame(name: Name, developer: Name, genres: Set<Genre>): GameIdentifier {
        val storageGame = storage.game

        if (storageGame.isGameNameStored(name)) {
            throw ConflictException("Game name already exists")
        }

        if (!genres.all { it.toString() in Genre.genresList} ) {
            throw BadRequestException("Genres must be one of the following: ${Genre.genresList}")
        }

        val game = Game(0u, name, developer, genres)

        return storageGame.create(game)
    }
    fun getGameById(id: UInt): Game {
        return storage.game.getById(id) ?: throw NotFoundException("Game not found")
    }

    fun searchGames(genres: Set<Genre>?, developer: Name?, limit: UInt, skip: UInt): GameList {
        val gamesSearch = storage.game.getGamesSearch(genres, developer, limit, skip)
        return gamesSearch.ifEmpty { throw NotFoundException("No games were found") }
    }
}

typealias GameIdentifier = UInt

typealias GameList = List<Game>
