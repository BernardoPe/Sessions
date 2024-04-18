import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.domain.util.Name
import pt.isel.ls.storage.db.SessionsDataDBGame

class SessionsDataDBGameTest {

    private val sessionsDataDBGame = SessionsDataDBGame()

    @Test
    fun `create game successfully`() {
        // Arrange
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
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
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val isStored = sessionsDataDBGame.isGameNameStored(Name("Test Game"))
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
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), Name("Test Developer"), 10u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())
        assertEquals(1, result.second)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with no results returns empty list`() {
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), Name("Nonexistent Developer"), 10u, 0u)
        // Assert
        assertTrue(result.first.isEmpty())
        assertEquals(0, result.second)
    }

    @Test
    fun `get games search with no genres returns correct results`() {
        // Arrange
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(null, Name("Test Developer"), 10u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())
        assertEquals(1, result.second)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with no developer returns correct results`() {
        // Arrange
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), null, 10u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())
        assertEquals(1, result.second)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with no genres and developer returns correct results`() {
        // Arrange
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(null, null, 10u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())
        assertEquals(1, result.second)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with limit returns correct results`() {
        // Arrange
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), Name("Test Developer"), 1u, 0u)
        // Assert
        assertTrue(result.first.isNotEmpty())
        assertEquals(1, result.second)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get games search with skip returns correct results`() {
        // Arrange
        val game1 = Game(0u, Name("Test Game 1"), Name("Test Developer"), setOf(Genre("RPG")))
        val game2 = Game(0u, Name("Test Game 2"), Name("Test Developer"), setOf(Genre("RPG")))
        val id1 = sessionsDataDBGame.create(game1)
        val id2 = sessionsDataDBGame.create(game2)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), Name("Test Developer"), 10u, 1u)
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
        val game1 = Game(0u, Name("Test Game 1"), Name("Test Developer"), setOf(Genre("RPG")))
        val game2 = Game(0u, Name("Test Game 2"), Name("Test Developer"), setOf(Genre("RPG")))
        val id1 = sessionsDataDBGame.create(game1)
        val id2 = sessionsDataDBGame.create(game2)
        // Act
        val result = sessionsDataDBGame.getGamesSearch(setOf(Genre("RPG")), Name("Test Developer"), 1u, 1u)
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
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val result = sessionsDataDBGame.getAllGames()
        // Assert
        assertTrue(result.isNotEmpty())
        // Clean up
        sessionsDataDBGame.delete(id)
    }

    @Test
    fun `get all games with no results returns empty list`() {
        // Act
        val result = sessionsDataDBGame.getAllGames()
        // Assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `get game by id returns correct result`() {
        // Arrange
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
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
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
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
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
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
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val isDeleted = sessionsDataDBGame.delete(id)
        // Assert
        assertTrue(isDeleted)
        assertFalse(sessionsDataDBGame.isGameNameStored(Name("Test Game")))
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
        val game = Game(0u, Name("Test Game"), Name("Test Developer"), setOf(Genre("RPG")))
        val id = sessionsDataDBGame.create(game)
        // Act
        val isDeleted = sessionsDataDBGame.delete(9999u)
        // Assert
        assertFalse(isDeleted)
        // Clean up
        sessionsDataDBGame.delete(id)
    }

}