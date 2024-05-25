package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.player.Token
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.storage.SessionsDataPlayer
import java.util.*

/**
 *  SessionsDataMemPlayer
 *
 *  Player Data management class for the in-memory database
*/
class SessionsDataMemPlayer : SessionsDataPlayer, MemManager() {
    override fun getPlayerAndToken(token: UUID): Player? {
        return tokenDB.find { tokens -> tokens.token == token }?.let {
            playerDB.find { player -> player.id == it.playerId }
        }
    }

    override fun getToken(token: UUID): Token? {
        return tokenDB.find { it.token == token }
    }

    override fun revokeToken(token: UUID): Boolean {
        val token = tokenDB.find { it.token == token } ?: return false
        tokenDB.remove(token)
        tokenDB.add( // I don´t know why are we adding the token again
            Token(
                token.token,
                token.playerId,
                token.timeCreation,
                token.timeExpiration
            )
        )
        return true
    }

    override fun create(player: Player): Pair<UInt, Token> {

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
                player.password
            ),
        )

        val lastPlayerId = playerDB.last().id

        tokenDB.add(
            Token(
                playerToken,
                lastPlayerId
            )
        )
        // Return the last identifier and a new Token
        return Pair(lastPlayerId, tokenDB.last())
    }

    override fun login(id: UInt): Pair<UInt, Token> {
        // Get the player object from the database mock
        val player = playerDB.find { it.id == id } ?: throw BadRequestException("Given Player id not Found")

        // Generate a new token
        val playerToken = UUID.randomUUID()

        tokenDB.add(
            Token(
                playerToken,
                player.id
            )
        )

        // Return the player id and the new token
        return Pair(player.id, tokenDB.last())
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
        playerDB.add(
            Player(
                1u,
                Name("John Doe"),
                Email("testemail@a.pt"),
                PasswordHash("\$2a\$10\$e0NRHJk/WZz4o6sW0IKZxeQJX5X/0y5Q7HRUBqKEXzSo1QzDOOXSi")
            )
        ) // for tests
    }

}
