package pt.isel.ls.Storage

import pt.isel.ls.DTO.Player.Player
import pt.isel.ls.Storage.Mem.SessionsDataMemPlayer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SessionsDataGamePlayerTest {

    @Test
    fun testCreateAndReadPlayer() {
        // Create a player
        val player = Player(1, "player", "email@test.com")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.create(player)
        // Check if the player was created
        // compare the player with the player read from the storage
        assertEquals(player, playerStorage.getById(1))
        // Check the player data
        // Start by reading the player from the storage
        val playerData = playerStorage.getById(1)
        // Check the player id
        assertEquals(1, playerData?.pid)
        // Check the player name
        assertEquals("player", playerData?.name)
        // Check the player email
        assertEquals("email@test.com", playerData?.email)
        // Check if the player with id 2 was not created
        assertNull(playerStorage.getById(2))
    }

    @Test
    fun testUpdatePlayer() {
        // Create a player
        val player = Player(1, "player", "email@test.com")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.create(player)
        // Update the player
        val newPlayer = Player(1, "newPlayer", "newEmail@test.com")
        playerStorage.update(1, newPlayer)
        // Check if the player was updated
        // compare the player with the player read from the storage
        assertEquals(newPlayer, playerStorage.getById(1))
        // Check the player data
        // Start by reading the player from the storage
        val playerData = playerStorage.getById(1)
        // Check the player id
        assertEquals(1, playerData?.pid)
        // Check the player name
        assertEquals("newPlayer", playerData?.name)
        // Check the player email
        assertEquals("newEmail@test.com", playerData?.email)
    }

    @Test
    fun testDeletePlayer() {
        // Create a player
        val player = Player(1, "player", "email@test.com")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.create(player)
        // Delete the player
        playerStorage.delete(0)
        // Check if the player was deleted
        assertNull(playerStorage.getById(0))
    }
}