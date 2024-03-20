package pt.isel.ls.storage

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.exceptions.GameNotFoundException
import pt.isel.ls.storage.mem.SessionsDataMemGame
import kotlin.test.*

/**
class SessionsDataGameTest {

    @Test
    fun createAndReadGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game", "developer", setOf("genre1", "genre2"))
        // Check if the game was created
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(0)
        // Check the game data
        // Check the game id
        assertEquals(0, gameData?.gid)
        // Check the game name
        assertEquals("game", gameData?.name)
        // Check the game developer
        assertEquals("developer", gameData?.developer)
        // Check the game genres
        assertEquals(setOf("genre1", "genre2"), gameData?.genres)
    }

    @Test
    fun readGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // add a game to the storage
        gameStorage.create("game", "developer", setOf("genre1", "genre2"))
        // read the game from the storage
        val gameData = gameStorage.getGamesSearch(setOf("genre1"), "developer", 2, 0)
        // Check the game data
        // Check if gameData is not null
        assertNotNull(gameData)
        // Check the game id
        assertEquals(0, gameData[0].gid)
        // Check the game name
        assertEquals("game", gameData[0].name)
        // Check the game developer
        assertEquals("developer", gameData[0].developer)
        // Check the game genres
        assertEquals(setOf("genre1", "genre2"), gameData[0].genres)
    }

    @Test
    fun updateGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game", "developer", setOf("genre1", "genre2"))
        // Update the game
        // Start by creating a new game object
        val newGame = Game(0, "newGame", "newDeveloper", setOf("newGenre1", "newGenre2"))
        gameStorage.update(0, newGame)
        // Check if the game was updated
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(0)
        // Check the game data
        // Check the game id
        assertEquals(0, gameData?.gid)
        // Check the game name
        assertEquals("newGame", gameData?.name)
        // Check the game developer
        assertEquals("newDeveloper", gameData?.developer)
        // Check the game genres
        assertEquals(setOf("newGenre1", "newGenre2"), gameData?.genres)
    }

    @Test
    fun deleteGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game", "developer", setOf("genre1", "genre2"))
        // Delete the game
        gameStorage.delete(0)
        // Check if the game was deleted
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(0)
        // Check if the game data is null
        assertNull(gameData)
    }

    @Test
    fun readGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // read the game from the storage
        val gameData = gameStorage.getById(0)
        // Check if gameData is null
        assertNull(gameData)
    }

    @Test
    fun readGameSearchNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // read the game from the storage
        val gameData = gameStorage.getGamesSearch(setOf("genre1"), "developer", 2, 0)
        // Check if gameData is empty
        assertEquals(0, gameData.size)
    }

    @Test
    fun updateGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Update the game
        // Start by creating a new game object
        val newGame = Game(0, "newGame", "newDeveloper", setOf("newGenre1", "newGenre2"))
        assertFailsWith<GameNotFoundException> {
            gameStorage.update(0, newGame)
        }
    }

    @Test
    fun deleteGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Delete the game
        assertFailsWith<GameNotFoundException> {
            gameStorage.delete(0)
        }
    }

    @Test
    fun isGameNameStoredTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game", "developer", setOf("genre1", "genre2"))
        // Check if the game name is stored
        assertTrue(gameStorage.isGameNameStored("game"))
    }

    @Test
    fun isGenresStoredTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game", "developer", setOf("genre1", "genre2"))
        // Check if the genres are stored
        assertTrue(gameStorage.isGenresStored(setOf("genre1", "genre2")))
    }

    @Test
    fun isDeveloperStoredTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game", "developer", setOf("genre1", "genre2"))
        // Check if the developer is stored
        assertTrue(gameStorage.isDeveloperStored("developer"))
    }
}*/