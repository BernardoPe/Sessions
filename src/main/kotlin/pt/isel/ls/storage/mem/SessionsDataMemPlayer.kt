package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.util.Email
import pt.isel.ls.data.domain.util.Name
import pt.isel.ls.storage.SessionsDataPlayer
import java.util.*

/**
 *  SessionsDataMemPlayer
 *
 *  Player Data management class for the in-memory database
*/
class SessionsDataMemPlayer : SessionsDataPlayer {

    /**
     * Database Mock
     *
     * This is a mockup of the database, used for testing purposes.
     * It is a mutable list of player objects
     *
     * @property db The database.
     */
    private var db: MutableList<Player> = mutableListOf(
        Player(1u, Name("John Doe"), Email("testemail@a.pt"), UUID.fromString("00000000-0000-0000-0000-000000000000").hash()), // for tests
    )

    /**
     * Last Identifier
     *
     * The last identifier is used to keep track of the last identifier used in the database mock
     * When a new player instance is added to the database mock, the last identifier is incremented
     *
     * @property lastId The last identifier.
     */
    private var lastId = 2u

    private fun UUID.hash(): Long {
        return mostSignificantBits xor leastSignificantBits
    }

    override fun getByToken(token: UUID): Player? {
        return db.find { it.token == token.hash() }
    }

    override fun isNameStored(name: Name): Boolean {
        return db.any { it.name == name }
    }

    override fun create(player: Player): Pair<UInt, UUID> {
        // Add the player object to the database mock

        val playerToken = UUID.randomUUID()

        db.add(
            Player(
                lastId,
                player.name,
                player.email,
                playerToken.hash(),
            ),
        )
        // Return the last identifier and a new UUID
        return Pair(lastId++, playerToken)
    }

    override fun isEmailStored(email: Email): Boolean {
        // Check if the player email exists in the database mock
        return db.any { it.email == email }
    }

    override fun getPlayersSearch(name: Name?, limit: UInt, skip: UInt): Pair<List<Player>, Int> {

        var players = db.toList()

        name?.let {
            players = players.filter { it.name.toString().startsWith(name.toString(), ignoreCase = true) }
        }

        val total = players.size

        return Pair(players.drop(skip.toInt()).take(limit.toInt()), total)
    }

    override fun getById(id: UInt): Player? {
        // Read the player object from the database mock
        db.forEach {
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
        return db
    }

    override fun update(id: UInt, value: Player): Boolean {
        // Update the player object in the database mock
        db.forEach {
            // search for the player with the given id
            if (it.id == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
                // add the new player to the database mock
                db.add(value)
                return true
            }
        }
        return false
    }

    override fun delete(id: UInt): Boolean {
        // Delete the player object from the database mock
        db.forEach {
            // search for the player with the given id
            if (it.id == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
                return true
            }
        }
        return false
    }
}
