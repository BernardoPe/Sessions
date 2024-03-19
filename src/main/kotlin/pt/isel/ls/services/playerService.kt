package pt.isel.ls.services

import pt.isel.ls.domain.player.Player
import pt.isel.ls.storage.SessionsDataPlayer
import java.util.*

class playerService(val storage : SessionsDataPlayer) {
    fun createPlayer(name: String, email: String): Pair<Int, UUID> {
        TODO()
    }

    /** May not be necessary to implement */
//    fun authenticatePlayer(token: String) : Boolean {
//        TODO()
//    }

    fun getPlayerDetails(pid: Int) : Player {
        TODO()
    }

    /** More methods to come */
}