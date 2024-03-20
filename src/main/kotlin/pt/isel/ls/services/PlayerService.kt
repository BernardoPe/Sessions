package pt.isel.ls.services

import pt.isel.ls.exceptions.services.PlayerCreationException
import pt.isel.ls.exceptions.services.PlayerCreationResult
import pt.isel.ls.exceptions.services.PlayerDetailsException
import pt.isel.ls.exceptions.services.PlayerDetailsResult
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.success

class PlayerService(val storage: SessionsDataManager) {
    fun createPlayer(name: String, email: String): PlayerCreationResult {
        if (!isSafeEmail(email)) {
            return failure(PlayerCreationException.UnsafeEmail)
        }
        return storage.apply {
            val storagePlayer = it.storagePlayer
            if (storagePlayer.isEmailStored(email)) {
                failure(PlayerCreationException.EmailAlreadyExists)
            } else {
                success(storagePlayer.create(name, email))
            }
        }
    }

    fun getPlayerDetails(pid: Int): PlayerDetailsResult {
        return storage.apply {
            val getPlayer = it.storagePlayer.getById(pid)
            if (getPlayer == null) {
                failure(PlayerDetailsException.PlayerNotFound)
            } else {
                success(getPlayer)
            }
        }
    }

    private fun isSafeEmail(email: String): Boolean =
        email.length in 3..32
                && email.contains(Regex("[a-zA-Z0-9]"))
                && email.contains('@')
                && email.contains(".com")

}

