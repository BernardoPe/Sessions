package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.storage.SessionsDataPlayer
import java.util.*

/**
 *  SessionsDataMemPlayer
 *
 *  Player Data management class for the in-memory database
*/
class SessionsDataMemPlayer : SessionsDataPlayer, MemManager() {

    private fun UUID.hash(): Long {
        return mostSignificantBits xor leastSignificantBits
    }

    override fun getByToken(token: UUID): Player? {
        return playerDB.find { it.token == token.hash() }
    }

    override fun isEmailStored(email: Email): Boolean {
       return playerDB.any { it.email == email }
    }

    override fun isNameStored(name: Name): Boolean {
        return playerDB.any { it.name == name }
    }

    override fun create(player: Player): Pair<UInt, UUID> {

        if (playerDB.any { it.email == player.email }) {
            throw BadRequestException("Given Player email already exists")
        }

        if (playerDB.any { it.name == player.name }) {
            throw BadRequestException("Given Player name already exists")
        }

        // Add the player object to the database mock
        val playerToken = UUID.randomUUID()

        playerDB.add(
            Player(
                pid,
                player.name,
                player.email,
                playerToken.hash(),
            ),
        )
        // Return the last identifier and a new UUID
        return Pair(pid - 1u, playerToken)
    }

    override fun getPlayersSearch(name: Name?, limit: UInt, skip: UInt): Pair<List<Player>, Int> {

        var players = playerDB.toList()

        name?.let {
            players = players.filter { it.name.toString().startsWith(name.toString(), ignoreCase = true) }
        }

        val total = players.size

        return Pair(players.drop(skip.toInt()).take(limit.toInt()), total)
    }

    override fun getById(id: UInt): Player? {
        // Read the player object from the database mock
        playerDB.forEach {
            // search for the player with the given id
            if (it.id == id) {
                // if found
                // return the player object
                return it
            }
        }
        return null
    }

    override fun getAll(): List<Player> {
        // Read all the player objects from the database mock
        return playerDB
    }

    override fun update(id: UInt, value: Player): Boolean {
        // Update the player object in the database mock
        playerDB.forEachIndexed { index, player ->
            // search for the player with the given id
            if (player.id == id) {
                // if found
                // remove the player from the database mock
                playerDB.removeAt(index)
                // add the new player to the database mock
                playerDB.add(value)
                return true
            }
        }
        return false
    }

    override fun delete(id: UInt): Boolean {
        // Delete the player object from the database mock
        playerDB.forEachIndexed { index, it ->
            // search for the player with the given id
            if (it.id == id) {
                // if found
                // remove the player from the database mock
                playerDB.removeAt(index)
                return true
            }
        }
        return false
    }

    override fun clear() {
        playerDB.clear()
        playerDB.add(Player(1u, Name("John Doe"), Email("testemail@a.pt"), 0L)) // for tests
    }

}
