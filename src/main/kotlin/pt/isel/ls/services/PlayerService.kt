package pt.isel.ls.services

import pt.isel.ls.data.domain.Email
import pt.isel.ls.data.domain.Name
import pt.isel.ls.exceptions.services.PlayerCreationException
import pt.isel.ls.exceptions.services.PlayerCreationResult
import pt.isel.ls.exceptions.services.PlayerDetailsException
import pt.isel.ls.exceptions.services.PlayerDetailsResult
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.utils.failure
import pt.isel.ls.utils.success
import java.util.UUID

class PlayerService(val storage: SessionsDataManager) {
    fun createPlayer(name: Name, email: Email): PlayerCreationResult {
        val storagePlayer = storage.player
        return if (storagePlayer.isEmailStored(email)) {
            failure(PlayerCreationException.EmailAlreadyExists)
        } else {
            success(storagePlayer.create(name, email))
        }
    }

    fun authenticatePlayer(token : UUID) : Boolean {
        return storage.player.getByToken(token) != null
    }

    fun getPlayerDetails(pid: UInt): PlayerDetailsResult {
        val getPlayer = storage.player.getById(pid)
        return if (getPlayer == null) {
            failure(PlayerDetailsException.PlayerNotFound)
        } else {
            success(getPlayer)
        }
    }

}

