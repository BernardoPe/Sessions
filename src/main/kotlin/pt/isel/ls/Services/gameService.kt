package pt.isel.ls.Services

import pt.isel.ls.Storage.SessionsData
import pt.isel.ls.DTO.Game.Game


class gameService(val storage: SessionsData<Game>) {

    fun createGame(game: Game) {
        storage.create(game)
    }
    fun getGameById(id: Int) {
        val game = storage.get(id)
    }

    fun detailsGame() {
        TODO()
    }

    fun listGames() {
        TODO()
    }

    /** More methods to come */

}