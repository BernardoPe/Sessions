package pt.isel.ls.services

import pt.isel.ls.domain.player.Player
import pt.isel.ls.dto.player.PlayerRequest

class playerService {
    fun createPlayer (player : PlayerRequest) : Pair<Int, String> {
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