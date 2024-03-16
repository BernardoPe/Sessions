package pt.isel.ls.services

import pt.isel.ls.domain.player.Player

class playerService {
    fun createPlayer(name: String, email: String): Pair<Int, String> {
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