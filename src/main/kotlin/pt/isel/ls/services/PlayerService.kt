package pt.isel.ls.services

import pt.isel.ls.exceptions.services.PlayerCreationException
import pt.isel.ls.exceptions.services.PlayerCreationResult
import pt.isel.ls.exceptions.services.PlayerDetailsException
import pt.isel.ls.exceptions.services.PlayerDetailsResult
import pt.isel.ls.storage.SessionsDataPlayer
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.success

class PlayerService(val storage: SessionsDataPlayer) {
    fun createPlayer(name: String, email: String): PlayerCreationResult {
        return if (!isSafeEmail(email)) {
            failure(PlayerCreationException.UnsafeEmail)
        } else if (storage.isEmailStored(email)) {
            failure(PlayerCreationException.EmailAlreadyExists)
        } else {
            success(storage.create(name, email))
        }
    }

    /** May not be necessary to implement */
//    fun authenticatePlayer(token: String) : Boolean {
//
//    }

    fun getPlayerDetails(pid: Int): PlayerDetailsResult {
        val getPlayer = storage.getById(pid)
        return if (getPlayer == null) {
            failure(PlayerDetailsException.PlayerNotFound)
        } else {
            success(getPlayer)
        }
    }

    private fun isSafeEmail(email: String): Boolean =
        email.length in 3..32
                && email.contains(Regex("[a-zA-Z0-9]"))
                && email.contains('@')
                && email.contains(".com")

}