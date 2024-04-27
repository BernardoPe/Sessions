package pt.isel.ls.storage.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.domain.util.Name

class SessionsDataDBGameTest {

    private val dbURL = System.getenv("JDBC_DEVELOPMENT_DATABASE_URL")
    private val sessionsDataDBGame = SessionsDataDBGame(dbURL)

    private val TEST_NAME = Name("Test Game that no one else will use")
    private val TEST_DEVELOPER = Name("Test Developer that no one else will use")

    @Test
    fun `create game successfully`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        // Act
        val id = sessionsDataDBGame.create(game)
        // Assert
        assertTrue(id > 0u)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `game name is stored`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val isStored = sessionsDataDBGame.isGameNameStored(TEST_NAME)
        // Assert
        assertTrue(isStored)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `game name is not stored`() {
        // Act
        val isStored = sessionsDataDBGame.isGameNameStored(Name("Nonexistent Game"))
        // Assert
        assertFalse(isStored)
    }

    @Test
    fun `get games search returns correct results`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), TEST_DEVELOPER, null, 10u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())

        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with no results returns empty list`() {
        // Act
        val result =
            sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), Name("Nonexistent Developer"), null, 10u, 0u)
        // Assert
        assertTrue(result.first.isEmpty())
        assertEquals(0, result.second)
    }

    @Test
    fun `get games search with no genres returns correct results`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(null, TEST_DEVELOPER, null, 10u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())

        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with no developer returns correct results`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), null, null, 10u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())

        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with no genres and developer returns correct results`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(null, null, null, 10u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with limit returns correct results`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), TEST_DEVELOPER, null, 1u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())

        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with skip returns correct results`() {
        // Arrange
        val game1 = Game(0u, Name("Test Game 1"), TEST_DEVELOPER, setOf(Genre("RPG")))
        val game2 = Game(0u, Name("Test Game 2"), TEST_DEVELOPER, setOf(Genre("RPG")))
        val id1 = sessionsDataDBGame.create(game1)
        val id2 = sessionsDataDBGame.create(game2)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), TEST_DEVELOPER, null, 10u, 1u)
        // Assert
        assertTrue(result.first.isNotEmpty())
        assertEquals(2, result.second)
        // Clean up
        sessionsDataDBGame.delete(id1)
        sessionsDataDBGame.delete(id2)
    }

    @Test
    fun `get games search with limit and skip returns correct results`() {
        // Arrange
        val game1 = Game(0u, Name("Test Game 1"), TEST_DEVELOPER, setOf(Genre("RPG")))
        val game2 = Game(0u, Name("Test Game 2"), TEST_DEVELOPER, setOf(Genre("RPG")))
        val id1 = sessionsDataDBGame.create(game1)
        val id2 = sessionsDataDBGame.create(game2)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), TEST_DEVELOPER, null, 1u, 1u)
        // Assert
        assertTrue(result.first.isNotEmpty())
        assertEquals(2, result.second)
        // Clean up
        sessionsDataDBGame.delete(id1)
        sessionsDataDBGame.delete(id2)
    }

    @Test
    fun `get all games returns correct results`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getAllGames()
        // Assert
        assertTrue(result.isNotEmpty())
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get game by id returns correct result`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getById(id)
        // Assert
        assertNotNull(result)
        assertEquals(game.name, result!!.name)
        assertEquals(game.developer, result.developer)
        assertEquals(game.genres, result.genres)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get game by nonexistent id returns null`() {
        // Act
        val result = sessionsDataDBGame.getById(9999u)
        // Assert
        assertNull(result)
    }

    @Test
    fun `update game successfully`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        val updatedGame = Game(id, Name("Updated Game"), Name("Updated Developer"), setOf(Genre("Adventure")))
        // Act
        val isUpdated = sessionsDataDBGame.update(updatedGame)
        // Assert
        val result = sessionsDataDBGame.getById(id)
        assertTrue(isUpdated)
        assertEquals(updatedGame.name, result!!.name)
        assertEquals(updatedGame.developer, result.developer)
        assertEquals(updatedGame.genres, result.genres)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `update nonexistent game returns false`() {
        // Arrange
        val updatedGame = Game(9999u, Name("Updated Game"), Name("Updated Developer"), setOf(Genre("Adventure")))
        // Act
        val isUpdated = sessionsDataDBGame.update(updatedGame)
        // Assert
        assertFalse(isUpdated)
    }

    @Test
    fun `update game with nonexistent id returns false`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        val updatedGame = Game(9999u, Name("Updated Game"), Name("Updated Developer"), setOf(Genre("Adventure")))
        // Act
        val isUpdated = sessionsDataDBGame.update(updatedGame)
        // Assert
        assertFalse(isUpdated)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `delete game successfully`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val isDeleted = sessionsDataDBGame.delete(id)
        // Assert
        assertTrue(isDeleted)
        assertFalse(sessionsDataDBGame.isGameNameStored(TEST_NAME))
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `delete nonexistent game returns false`() {
        // Act
        val isDeleted = sessionsDataDBGame.delete(9999u)
        // Assert
        assertFalse(isDeleted)
    }

    @Test
    fun `delete game with nonexistent id returns false`() {
        // Arrange
        val game = Game(0u, TEST_NAME, TEST_DEVELOPER, setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val isDeleted = sessionsDataDBGame.delete(9999u)
        // Assert
        assertFalse(isDeleted)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

}