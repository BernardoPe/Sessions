package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Game.Game


class SessionsDataMemGame : SessionsDataMem<Game>() {
    fun createGame(game: Game) {
        create(game)
    }

    fun readGame(id: Int): Game? {
        return read(id)
    }

    fun updateGame(id: Int, game: Game) {
        update(id, game)
    }

    fun deleteGame(id: Int) {
        delete(id)
    }
}