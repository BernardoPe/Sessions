package pt.isel.ls.storage.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import java.util.*

class SessionsDataDBPlayerTest {

    private val dbURL = System.getenv("JDBC_DEVELOPMENT_DATABASE_URL")
    private val sessionsDataDBPlayer = SessionsDataDBPlayer(dbURL)

    // Constants
    private val TEST_EMAIL = "testEmailthatnooneelsewilluse@dont.com"
    private val TEST_NAME = "Test Name that no one else will use"
    private val TEST_TOKEN = 1L
    private val TEST_ID = 1u
    private val TEST_UPDATED_NAME = "Updated Name that no one else will use"
    private val TEST_UPDATED_EMAIL = "updatedtestthatnooneelsewilluse@dont.com"
    private val TEST_UPDATED_TOKEN = 2L
    private val NONEXISTENT_EMAIL = "unexistentemailthatnoonseelsewilluse@dont.com"
    private val NONEXISTENT_ID = 9999u

    @Test
    fun `create player and verify by id`() {
        // Arrange
        val player = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        // Act
        val (id, _) = sessionsDataDBPlayer.create(player)
        // Assert
        val retrievedPlayer = sessionsDataDBPlayer.getById(id)
        assertNotNull(retrievedPlayer)
        assertEquals(player.name, retrievedPlayer?.name)
        assertEquals(player.email, retrievedPlayer?.email)
        // Clean up
        sessionsDataDBPlayer.delete(id)
    }

    @Test
    fun `get existent player by id`() {
        // Arrange
        val player = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val (id, _) = sessionsDataDBPlayer.create(player)
        // Act
        val retrievedPlayer = sessionsDataDBPlayer.getById(id)
        // Assert
        assertNotNull(retrievedPlayer)
        assertEquals(player.name, retrievedPlayer?.name)
        assertEquals(player.email, retrievedPlayer?.email)
        // Clean up
        sessionsDataDBPlayer.delete(id)
    }

    @Test
    fun `get non-existent player by id`() {
        // Act
        val retrievedPlayer = sessionsDataDBPlayer.getById(NONEXISTENT_ID)
        // Assert
        assertNull(retrievedPlayer)
    }

    @Test
    fun `check email stored`() {
        // Arrange
        val player = Player(TEST_ID, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val (id) = sessionsDataDBPlayer.create(player)
        // Act
        val isStored = sessionsDataDBPlayer.isEmailStored(Email(TEST_EMAIL))
        // Assert
        assertEquals(true, isStored)
        // Clean up
        sessionsDataDBPlayer.delete(id)
    }

    @Test
    fun `check email not stored`() {
        // Act
        val isStored = sessionsDataDBPlayer.isEmailStored(Email(NONEXISTENT_EMAIL))
        // Assert
        assertEquals(false, isStored)
    }

    @Test
    fun `get all players`() {
        // Arrange
        val player1 = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val player2 = Player(0u, Name(TEST_UPDATED_NAME), Email(TEST_UPDATED_EMAIL), TEST_UPDATED_TOKEN)
        val (id1, _) = sessionsDataDBPlayer.create(player1)
        val (id2, _) = sessionsDataDBPlayer.create(player2)
        // Act
        val players = sessionsDataDBPlayer.getAll()
        // Assert
        assertTrue(players.isNotEmpty())
        // Clean up
        sessionsDataDBPlayer.delete(id1)
        sessionsDataDBPlayer.delete(id2)
    }

    @Test
    fun `get all players empty after delete`() {
        // Arrange
        val player = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val (id, _) = sessionsDataDBPlayer.create(player)
        sessionsDataDBPlayer.delete(id)
        // Act
        val players = sessionsDataDBPlayer.getAll()
        // Assert
        assertFalse(players.contains(player))
    }

    @Test
    fun `update player`() {
        // Arrange
        val player = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val (id, _) = sessionsDataDBPlayer.create(player)
        val updatedPlayer = Player(id, Name(TEST_UPDATED_NAME), Email(TEST_UPDATED_EMAIL), TEST_UPDATED_TOKEN)
        // Act
        val isUpdated = sessionsDataDBPlayer.update(id, updatedPlayer)
        // Assert
        assertTrue(isUpdated)
        val retrievedPlayer = sessionsDataDBPlayer.getById(id)
        assertEquals(updatedPlayer.name, retrievedPlayer?.name)
        assertEquals(updatedPlayer.email, retrievedPlayer?.email)
        // Clean up
        sessionsDataDBPlayer.delete(id)
    }

    @Test
    fun `update non-existent player`() {
        // Arrange
        Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val updatedPlayer = Player(NONEXISTENT_ID, Name(TEST_UPDATED_NAME), Email(TEST_UPDATED_EMAIL), TEST_UPDATED_TOKEN)
        // Act
        val isUpdated = sessionsDataDBPlayer.update(NONEXISTENT_ID, updatedPlayer)
        // Assert
        assertFalse(isUpdated)
    }

    @Test
    fun `delete player`() {
        // Arrange
        val player = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val (id, _) = sessionsDataDBPlayer.create(player)
        // Act
        val isDeleted = sessionsDataDBPlayer.delete(id)
        // Assert
        assertTrue(isDeleted)
        val retrievedPlayer = sessionsDataDBPlayer.getById(id)
        assertNull(retrievedPlayer)
    }

    @Test
    fun `delete non-existent player`() {
        // Act
        val isDeleted = sessionsDataDBPlayer.delete(NONEXISTENT_ID)
        // Assert
        assertFalse(isDeleted)
    }

    @Test
    fun `get player by token`() {
        // Arrange
        val player = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val (id, token) = sessionsDataDBPlayer.create(player)
        // Act
        val retrievedPlayer = sessionsDataDBPlayer.getByToken(token)
        // Assert
        assertNotNull(retrievedPlayer)
        assertEquals(player.name, retrievedPlayer?.name)
        assertEquals(player.email, retrievedPlayer?.email)
        // Clean up
        sessionsDataDBPlayer.delete(id)
    }

    @Test
    fun `get non-existent player by token`() {
        // Act
        val retrievedPlayer = sessionsDataDBPlayer.getByToken(UUID.randomUUID())
        // Assert
        assertNull(retrievedPlayer)
    }

    @Test
    fun `get player by non-existent token`() {
        // Arrange
        val player = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val (id) = sessionsDataDBPlayer.create(player)
        // Act
        val retrievedPlayer = sessionsDataDBPlayer.getByToken(UUID.randomUUID())
        // Assert
        assertNull(retrievedPlayer)
        // Clean up
        sessionsDataDBPlayer.delete(id)
    }

    @Test
    fun `get player by non-existent token and non-existent player`() {
        // Act
        val retrievedPlayer = sessionsDataDBPlayer.getByToken(UUID.randomUUID())
        // Assert
        assertNull(retrievedPlayer)
    }

    @Test
    fun `get player by token after update and delete`() {
        // Arrange
        val player = Player(0u, Name(TEST_NAME), Email(TEST_EMAIL), TEST_TOKEN)
        val (id, token) = sessionsDataDBPlayer.create(player)
        val updatedPlayer = Player(player.id, Name(TEST_UPDATED_NAME), Email(TEST_UPDATED_EMAIL), TEST_UPDATED_TOKEN)
        sessionsDataDBPlayer.update(player.id, updatedPlayer)
        sessionsDataDBPlayer.delete(id)
        // Act
        val retrievedPlayer = sessionsDataDBPlayer.getByToken(token)
        // Assert
        assertNull(retrievedPlayer)
    }

}