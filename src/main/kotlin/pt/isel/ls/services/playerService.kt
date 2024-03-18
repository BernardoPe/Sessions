package pt.isel.ls.services

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.storage.SessionsDataPlayer

class playerService(val storage : SessionsDataPlayer) {
    fun createPlayer(name: String, email: String): PlayerCredentials {
        TODO()
    }

    fun authenticatePlayer(token: String) : Boolean {
        TODO()
    }

    fun getPlayerDetails(pid: Int) : Player {
        TODO()
    }

    /** More methods to come */
}

typealias PlayerCredentials = Pair<Int, String>