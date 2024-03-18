package pt.isel.ls.services

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.storage.SessionsDataGame


class gameService(val storage: SessionsDataGame) {

    fun createGame(name: String, developer: String, genres: Set<String>): GameIdentifier {
        //storage.create(game)
        TODO()
    }
    fun getGameById(id: Int) : Game {
        //val game = storage.getById(id)
        TODO()
    }

    fun searchGames(genres: Set<String>, developer: String, limit: Int?, skip: Int?): GameSearchResult {
        TODO()
    }

    /** More methods to come */

}

typealias GameIdentifier = Int

typealias GameSearchResult = Set<Game>
