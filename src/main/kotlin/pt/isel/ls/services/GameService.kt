package pt.isel.ls.services

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.exceptions.ConflictException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager


class GameService(val storage: SessionsDataManager) {
    fun createGame(name: Name, developer: Name, genres: Set<Genre>): GameIdentifier {

        val storageGame = storage.game

        if (storageGame.isGameNameStored(name)) {
            throw ConflictException("Game name already exists")
        }

        return storageGame.create(name, developer, genres)

    }
    fun getGameById(id: UInt): Game {
        return storage.game.getById(id) ?: throw NotFoundException("Game not found")
    }

    fun searchGames(genres: Set<Genre>, developer: Name, limit: UInt, skip: UInt): GameList {
        val gamesSearch = storage.game.getGamesSearch(genres, developer, limit, skip)
            .filter { it.developer == developer }
        return gamesSearch.ifEmpty { throw NotFoundException("Developer not found") }
    }


}

typealias GameIdentifier = UInt

typealias GameList = List<Game>
