package pt.isel.ls.storage.mem

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.exceptions.PlayerEmailAlreadyExistsException
import pt.isel.ls.exceptions.PlayerNotFoundException
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
    private var db: MutableList<Player> = mutableListOf()

    /**
     * Last Identifier
     *
     * The last identifier is used to keep track of the last identifier used in the database mock
     * When a new player instance is added to the database mock, the last identifier is incremented
     *
     * @property lastId The last identifier.
     */
    private var lastId = 0

    override fun create(name: String, email: String): Pair<Int, UUID> {
        // Add the player object to the database mock
        // Set by checking if the player email already exists
        if (isEmailStored(email)) {
            throw PlayerEmailAlreadyExistsException("Given Player email already exists")
        }
        // Increment the last identifier
        lastId++
        // Add the updated player object to the database mock
        db.add(
            Player(
            lastId,
                name,
                email
            )
        )
        // Return the last identifier and a new UUID
        return Pair(lastId, UUID.randomUUID())
    }

    override fun isEmailStored(email: String): Boolean {
        // Check if the player email exists in the database mock
        return db.any{it.email == email}
    }

    override fun getById(id: Int): Player? {
        // Read the player object from the database mock
        db.forEach {
            // search for the player with the given id
            if (it.pid == id) {
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

    override fun update(id: Int, value: Player) {
        // Update the player object in the database mock
        db.forEach {
            // search for the player with the given id
            if (it.pid == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
                // add the new player to the database mock
                db.add(value)
            }
        }
        // alert the user that the player does not exist
        throw PlayerNotFoundException("Given Player does not exist")
    }

    override fun delete(id: Int) {
        // Delete the player object from the database mock
        db.forEach {
            // search for the player with the given id
            if (it.pid == id) {
                // if found
                // remove the player from the database mock
                db.remove(it)
            }
        }
        // alert the user that the player does not exist
        throw PlayerNotFoundException("Given Player does not exist")
    }
}