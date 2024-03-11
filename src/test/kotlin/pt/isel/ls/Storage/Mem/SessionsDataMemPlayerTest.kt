package pt.isel.ls.Storage.Mem

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import pt.isel.ls.DTO.Player.Player

class SessionsDataMemPlayerTest {

    @Test
    fun testCreateAndReadPlayer() {
        // Create a player
        val player = Player(1, "player", "email")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.createPlayer(player)
        // Check if the player was created
        // compare the player with the player read from the storage
        assertEquals(player, playerStorage.readPlayer(1))
        // Check the player data
        // Start by reading the player from the storage
        val playerData = playerStorage.readPlayer(1)
        // Check the player id
        assertEquals(1, playerData?.pid)
        // Check the player name
        assertEquals("player", playerData?.name)
        // Check the player email
        assertEquals("email", playerData?.email)
        // Check if the player with id 2 was not created
        assertNull(playerStorage.readPlayer(2))
    }

    @Test
    fun testUpdatePlayer() {
        // Create a player
        val player = Player(1, "player", "email")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.createPlayer(player)
        // Update the player
        val newPlayer = Player(1, "newPlayer", "newEmail")
        playerStorage.updatePlayer(1, newPlayer)
        // Check if the player was updated
        // compare the player with the player read from the storage
        assertEquals(newPlayer, playerStorage.readPlayer(1))
        // Check the player data
        // Start by reading the player from the storage
        val playerData = playerStorage.readPlayer(1)
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
        val player = Player(1, "player", "email")
        // Create a player storage
        val playerStorage = SessionsDataMemPlayer()
        // Create the player (add it to the storage)
        playerStorage.createPlayer(player)
        // Delete the player
        playerStorage.deletePlayer(0)
        // Check if the player was deleted
        assertNull(playerStorage.readPlayer(0))
    }
}