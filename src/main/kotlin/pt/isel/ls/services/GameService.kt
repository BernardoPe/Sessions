package pt.isel.ls.services

import pt.isel.ls.domain.game.Game
import pt.isel.ls.exceptions.services.*
import pt.isel.ls.storage.SessionsDataGame
import pt.isel.ls.utils.Either
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.success


class GameService(val storage: SessionsDataGame) {
    fun createGame(name: String, developer: String, genres: Set<String>): GameCreationResult {
        return if (storage.isGameNameStored(name)) {
            failure(GameCreationException.GameNameAlreadyExists)
        } else {
            success(storage.create(name, developer, genres))
        }
    }

    fun getGameById(id: Int): GameDetailsResult {
        val getGame = storage.getById(id)

        return if (getGame == null) {
            failure(GameDetailsException.GameNotFound)
        } else {
            success(getGame)
        }

    }

    fun searchGames(genres: Set<String>, developer: String, limit: Int, skip: Int): Either<GameSearchException, List<Game>> {
        return if (storage.isGenresStored(genres)) {
            failure(GameSearchException.GenresNotFound)
        } else if (storage.isDeveloperStored(developer)) {
            failure(GameSearchException.DeveloperNotFound)
        } else {
            success(storage.getGamesSearch(genres, developer, limit, skip))
        }
    }
}