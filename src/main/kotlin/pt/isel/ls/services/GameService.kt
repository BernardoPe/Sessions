package pt.isel.ls.services

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.exceptions.services.*
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.success


class GameService(val storage: SessionsDataManager) {
    fun createGame(name: Name, developer: Name, genres: Set<Genre>): GameIdentifier {

        val storageGame = storage.game

        if (storageGame.isGameNameStored(name)) {
            throw BadRequestException("Game name already exists")
        }

        return storageGame.create(name, developer, genres)

    }

    fun getGameById(id: UInt): Game {
        return storage.game.getById(id) ?: throw NotFoundException("Game not found")
    }

    fun searchGames(genres: Set<Genre>, developer: Name, limit: UInt, skip: UInt): GameList {

        val storageGame = storage.game

        if (!storageGame.isGenresStored(genres))
            throw NotFoundException("Genres not found")

        if (!storageGame.isDeveloperStored(developer))
            throw NotFoundException("Developer not found")


        return storageGame.getGamesSearch(genres, developer, limit, skip)

    }

}

typealias GameIdentifier = UInt

typealias GameList = List<Game>
