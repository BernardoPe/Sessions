package pt.isel.ls.storage

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
class SessionsDataPlayerTest {

    @Test
    fun createPlayerTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create("testPlayer", "testEmail")
        // Check if the player was added
        // Start by getting the player
        val player = playerStorage.getById(0)
        // Check if the player is the same
        // Check if the player is not null
        assert(player != null)
        // Check the pid
        assertEquals(0, player!!.pid)
        // Check the name
        assertEquals("testPlayer", player.name)
        // Check the email
        assertEquals("testEmail", player.email)
    }

    @Test
    fun updatePlayerTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create("testPlayer", "testEmail")
        // Update the player
        playerStorage.update(0, Player(0, "newName", "newEmail"))
        // Check if the player was updated
        // Start by getting the player
        val player = playerStorage.getById(0)
        // Check if the player is the same
        // Check if the player is not null
        assert(player != null)
        // Check the pid
        assertEquals(0, player!!.pid)
        // Check the name
        assertEquals("newName", player.name)
        // Check the email
        assertEquals("newEmail", player.email)
    }

    @Test
    fun deletePlayerTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create("testPlayer", "testEmail")
        // Delete the player
        playerStorage.delete(0)
        // Check if the player was deleted
        // Start by getting the player
        val player = playerStorage.getById(0)
        // Check if the player is null
        assertNull(player)
    }

    @Test
    fun isEmailStoredTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create("testPlayer", "testEmail")
        // Check if the email is stored
        // Check if the email is stored
        assert(playerStorage.isEmailStored("testEmail"))
    }

    @Test
    fun getAllPlayersTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create("testPlayer", "testEmail")
        // Get all players
        val players = playerStorage.getAll()
        // Check if the player is the same
        // Check if the player is not null
        assert(players.isNotEmpty())
        // Check the pid
        assertEquals(0, players[0].pid)
        // Check the name
        assertEquals("testPlayer", players[0].name)
        // Check the email
        assertEquals("testEmail", players[0].email)
    }

    @Test
    fun getPlayerByIdTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create("testPlayer", "testEmail")
        // Get the player
        val player = playerStorage.getById(0)
        // Check if the player is the same
        // Check if the player is not null
        assert(player != null)
        // Check the pid
        assertEquals(0, player!!.pid)
        // Check the name
        assertEquals("testPlayer", player.name)
        // Check the email
        assertEquals("testEmail", player.email)
    }

    @Test
    fun getPlayerByIdNotFoundTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Get the player
        val player = playerStorage.getById(0)
        // Check if the player is null
        assertNull(player)
    }
}*/