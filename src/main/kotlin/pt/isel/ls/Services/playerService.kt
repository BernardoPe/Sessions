package pt.isel.ls.Services

import org.eclipse.jetty.http.HttpTokens.Token
import pt.isel.ls.DTO.Player.Player
import pt.isel.ls.DTO.Player.PlayerRequest

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