package pt.isel.ls.Storage.Mem

import pt.isel.ls.DTO.Player.Player
class SessionsDataMemPlayer : SessionsDataMem<Player>() {
    fun createPlayer(Player: Player) {
        create(Player)
    }

    fun readPlayer(id: Int): Player? {
        return read(id)
    }

    fun updatePlayer(id: Int, player: Player) {
        update(id, player)
    }

    fun deletePlayer(id: Int) {
        delete(id)
    }
}