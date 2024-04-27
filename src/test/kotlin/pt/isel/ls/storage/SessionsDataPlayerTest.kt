package pt.isel.ls.storage

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SessionsDataPlayerTest {

    @Test
    fun createPlayerTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Check if the player was added
        // Start by getting the player
        val player = playerStorage.getById(2u)
        // Check if the player is the same
        // Check if the player is not null
        assert(player != null)
        // Check the id
        assertEquals(2u, player!!.id)
        // Check the name
        assertEquals("testPlayer".toName(), player.name)
        // Check the email
        assertEquals("testEmail@test.com".toEmail(), player.email)
    }

    @Test
    fun updatePlayerTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Update the player
        playerStorage.update(2u, Player(2u, "newName".toName(), "newEmail@test.com".toEmail(), 0L))
        // Check if the player was updated
        // Start by getting the player
        val player = playerStorage.getById(2u)
        // Check if the player is the same
        // Check if the player is not null
        assert(player != null)
        // Check the id
        assertEquals(2u, player!!.id)
        // Check the name
        assertEquals("newName".toName(), player.name)
        // Check the email
        assertEquals("newEmail@test.com".toEmail(), player.email)
    }

    @Test
    fun deletePlayerTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Delete the player
        playerStorage.delete(2u)
        // Check if the player was deleted
        // Start by getting the player
        val player = playerStorage.getById(2u)
        // Check if the player is null
        assertNull(player)
    }

    @Test
    fun isEmailStoredTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Check if the email is stored
        assert(playerStorage.isEmailStored("testEmail@test.com".toEmail()))
    }

    @Test
    fun isNameStoredTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Check if the name is stored
        assert(playerStorage.isNameStored("testPlayer".toName()))
    }

    @Test
    fun testPlayerNameSearch() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Check if the name is stored
        assert(playerStorage.isNameStored("testPlayer".toName()))

        val playerSearch = playerStorage.getPlayersSearch("tes".toName(), 10u, 0u)
        assertEquals(1, playerSearch.first.size)
        assertEquals(1, playerSearch.second)
        assertEquals(2u, playerSearch.first[0].id)
        assertEquals("testPlayer".toName(), playerSearch.first[0].name)
        assertEquals("testEmail@test.com".toEmail(), playerSearch.first[0].email)
    }

    @Test
    fun testPlayerNameSearchNotFound() {
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Check if the name is stored
        assert(playerStorage.isNameStored("testPlayer".toName()))
        // Search by name, characters are contained in the name but not from the start
        val playerSearch = playerStorage.getPlayersSearch("play".toName(), 10u, 0u)
        assertEquals(0, playerSearch.first.size)
        assertEquals(0, playerSearch.second)
    }

    @Test
    fun getAllPlayersTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Get all players
        val players = playerStorage.getAll()
        // Check if the player is the same
        // Check if the player is not null
        assert(players.isNotEmpty())
        // Check the id
        assertEquals(2u, players[1].id)
        // Check the name
        assertEquals("testPlayer".toName(), players[1].name)
        // Check the email
        assertEquals("testEmail@test.com".toEmail(), players[1].email)
    }

    @Test
    fun getPlayerByIdTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Add a player to the storage
        playerStorage.create(Player(0u, "testPlayer".toName(), "testEmail@test.com".toEmail(), 0L))
        // Get the player
        val player = playerStorage.getById(2u)
        // Check if the player is the same
        // Check if the player is not null
        assert(player != null)
        // Check the id
        assertEquals(2u, player!!.id)
        // Check the name
        assertEquals("testPlayer".toName(), player.name)
        // Check the email
        assertEquals("testEmail@test.com".toEmail(), player.email)
    }

    @Test
    fun getPlayerByIdNotFoundTest() {
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Get the player
        val player = playerStorage.getById(2u)
        // Check if the player is null
        assertNull(player)
    }
}
