package pt.isel.ls.services

import pt.isel.ls.data.domain.Email
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.exceptions.ConflictException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.storage.SessionsDataManager
import java.util.*

class PlayerService(val storage: SessionsDataManager) {
    fun createPlayer(name: Name, email: Email): PlayerCredentials {

        val storagePlayer = storage.player

        if (storagePlayer.isEmailStored(email))
            throw ConflictException("Given Player email already exists")

        return storagePlayer.create(name, email)

    }

    fun authenticatePlayer(token : UUID) : Boolean {
        return storage.player.getByToken(token) != null
    }

    fun getPlayerDetails(pid: UInt) : Player  {
        return storage.player.getById(pid) ?: throw NotFoundException("Player not found")
    }



}

typealias PlayerCredentials = Pair<UInt, UUID>
