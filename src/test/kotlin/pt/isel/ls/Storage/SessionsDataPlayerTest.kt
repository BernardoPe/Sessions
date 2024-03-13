package pt.isel.ls.Storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import pt.isel.ls.DTO.Player.Player
import pt.isel.ls.Storage.Mem.SessionsDataMemPlayer

class SessionsDataPlayerTest {

    @Test
    fun testCreateAndReadPlayer() {
        // Create a player
        val player = Player(1, "player", "email", "token")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.create(player)
        // Check if the player was created
        // compare the player with the player read from the storage
        assertEquals(player, playerStorage.get(1))
        // Check the player data
        // Start by reading the player from the storage
        val playerData = playerStorage.get(1)
        // Check the player id
        assertEquals(1, playerData?.pid)
        // Check the player name
        assertEquals("player", playerData?.name)
        // Check the player email
        assertEquals("email", playerData?.email)
        // Check if the player with id 2 was not created
        assertNull(playerStorage.get(2))
    }

    @Test
    fun testUpdatePlayer() {
        // Create a player
        val player = Player(1, "player", "email", "token")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.create(player)
        // Update the player
        val newPlayer = Player(1, "newPlayer", "newEmail", "newToken")
        playerStorage.update(1, newPlayer)
        // Check if the player was updated
        // compare the player with the player read from the storage
        assertEquals(newPlayer, playerStorage.get(1))
        // Check the player data
        // Start by reading the player from the storage
        val playerData = playerStorage.get(1)
        // Check the player id
        assertEquals(1, playerData?.pid)
        // Check the player name
        assertEquals("newPlayer", playerData?.name)
        // Check the player email
        assertEquals("newEmail", playerData?.email)
    }

    @Test
    fun testDeletePlayer() {
        // Create a player
        val player = Player(1, "player", "email", "token")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.create(player)
        // Delete the player
        playerStorage.delete(0)
        // Check if the player was deleted
        assertNull(playerStorage.get(0))
    }
}