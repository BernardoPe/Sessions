package pt.isel.ls.services

import pt.isel.ls.domain.game.Game
import pt.isel.ls.dto.game.GameRequest
import pt.isel.ls.dto.game.GameSearch


class gameService() {//val storage: SessionsDataGame) {

    fun createGame(game: GameRequest) {
        //storage.create(game)
        TODO()
    }
    fun getGameById(id: Int) : Game {
        //val game = storage.getById(id)
        TODO()
    }

    fun detailsGame() {
        TODO()
    }

    fun listGames(game : GameSearch, limit: Int, skip: Int) : List<Game> {
        TODO()
    }

    /** More methods to come */

}