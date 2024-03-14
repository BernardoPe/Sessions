package pt.isel.ls.Services

import pt.isel.ls.Storage.SessionsDataGame
import pt.isel.ls.DTO.Game.Game
import pt.isel.ls.DTO.Game.GameRequest
import pt.isel.ls.DTO.Game.GameSearch


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