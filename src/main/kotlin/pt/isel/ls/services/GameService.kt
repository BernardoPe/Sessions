package pt.isel.ls.services

import pt.isel.ls.exceptions.services.*
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.success


class GameService(val storage: SessionsDataManager) {
    fun createGame(name: String, developer: String, genres: Set<String>): GameCreationResult {
        return storage.apply {
            val storageGame = it.storageGame
            if (storageGame.isGameNameStored(name)) {
                failure(GameCreationException.GameNameAlreadyExists)
            } else {
                success(storageGame.create(name, developer, genres))
            }
        }
    }

    fun getGameById(id: Int): GameDetailsResult {
        return storage.apply {
            val getGame = it.storageGame.getById(id)
            if (getGame == null) {
                failure(GameDetailsException.GameNotFound)
            } else {
                success(getGame)
            }
        }
    }

    fun searchGames(genres: Set<String>, developer: String, limit: Int?, skip: Int?): GameSearchResult {
        return storage.apply {
            val storageGame = it.storageGame
            if (storageGame.isGenresStored(genres)) {
                failure(GameSearchException.GenresNotFound)
            } else if (storageGame.isDeveloperStored(developer)) {
                failure(GameSearchException.DeveloperNotFound)
            } else {
                success(storageGame.getGamesSearch(genres, developer))
            }
        }
    }
}