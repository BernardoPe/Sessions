package pt.isel.ls.services

import pt.isel.ls.domain.game.Game


class gameService() {//val storage: SessionsDataGame) {

    fun createGame(name: String, developer: String, genres: Set<String>): Int {
        //storage.create(game)
        TODO()
    }
    fun getGameById(id: Int) : Game {
        //val game = storage.getById(id)
        TODO()
    }

    fun searchGames(genres: Set<String>, developer: String, limit: Int?, skip: Int?): Set<Game> {
        TODO()
    }

    /** More methods to come */

}