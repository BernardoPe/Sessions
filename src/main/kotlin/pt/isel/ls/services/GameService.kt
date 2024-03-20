package pt.isel.ls.services

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.exceptions.services.*
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.success


class GameService(val storage: SessionsDataManager) {
    fun createGame(name: Name, developer: Name, genres: Set<Genre>): GameCreationResult {

            val storageGame = storage.game

            return if (storageGame.isGameNameStored(name)) {
                failure(GameCreationException.GameNameAlreadyExists)
            } else {
                success(storageGame.create(name, developer, genres))
            }

    }

    fun getGameById(id: UInt): GameDetailsResult {

        val getGame = storage.game.getById(id)

        return if (getGame == null) {
            failure(GameDetailsException.GameNotFound)
        } else {
            success(getGame)
        }

    }

    fun searchGames(genres: Set<Genre>, developer: Name, limit: UInt, skip: UInt): GameSearchResult {

            val storageGame = storage.game

            return if (storageGame.isGenresStored(genres)) {
                failure(GameSearchException.GenresNotFound)
            } else if (storageGame.isDeveloperStored(developer)) {
                failure(GameSearchException.DeveloperNotFound)
            } else {
                success(storageGame.getGamesSearch(genres, developer, limit, skip))
            }

    }

}