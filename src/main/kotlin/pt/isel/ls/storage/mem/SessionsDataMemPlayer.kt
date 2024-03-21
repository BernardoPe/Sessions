package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.Email
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
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
        Player(0u, Name("John Doe"), Email("testemail@a.pt"), UUID.fromString("00000000-0000-0000-0000-000000000000")), // for tests
    )

    /**
     * Last Identifier
     *
     * The last identifier is used to keep track of the last identifier used in the database mock
     * When a new player instance is added to the database mock, the last identifier is incremented
     *
     * @property lastId The last identifier.
     */
    private var lastId = 1u

    override fun getByToken(token: UUID): Player? {
        return db.find { it.token == token }
    }

    override fun create(name: Name, email: Email): Pair<UInt, UUID> {
        // Add the player object to the database mock
        // Set by checking if the player email already exists
        if (isEmailStored(email)) {
            throw BadRequestException("Given Player email already exists")
        }
        // Add the updated player object to the database mock

        val playerToken = UUID.randomUUID()

        db.add(
            Player(
                lastId,
                name,
                email,
                playerToken
            )
        )
        // Return the last identifier and a new UUID
        return Pair(lastId++, playerToken)
    }

    override fun isEmailStored(email: Email): Boolean {
        // Check if the player email exists in the database mock
        return db.any{it.email == email}
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

    override fun update(id: UInt, value: Player) {
        // Update the player object in the database mock
        db.forEach {
            // search for the player with the given id
            if (it.id == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
                // add the new player to the database mock
                db.add(value)
                return
            }
        }
        // alert the user that the player does not exist
        throw NotFoundException("Given Player does not exist")
    }

    override fun delete(id: UInt) {
        // Delete the player object from the database mock
        db.forEach {
            // search for the player with the given id
            if (it.id == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
                return
            }
        }
        // alert the user that the player does not exist
        throw NotFoundException("Given Player does not exist")
    }
}