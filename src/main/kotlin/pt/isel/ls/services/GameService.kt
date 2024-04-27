package pt.isel.ls.services

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.domain.util.Name
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager

class GameService(val storage: SessionsDataManager) {
    fun createGame(name: Name, developer: Name, genres: Set<Genre>): GameIdentifier {
        val storageGame = storage.game

        if (storageGame.isGameNameStored(name)) {
            throw BadRequestException("Game name already exists")
        }

        val game = Game(0u, name, developer, genres)

        return storageGame.create(game)
    }
    fun getGameById(id: UInt): Game {
        return storage.game.getById(id) ?: throw NotFoundException("Game not found")
    }

    fun searchGames(genres: Set<Genre>?, developer: Name?, name: Name?, limit: UInt, skip: UInt): Pair<GameList, Int> {
        val gamesSearch = storage.game.getGamesSearch(genres, developer, name, limit, skip)
        return Pair(gamesSearch.first, gamesSearch.second)
    }
}

typealias GameIdentifier = UInt

typealias GameList = List<Game>
