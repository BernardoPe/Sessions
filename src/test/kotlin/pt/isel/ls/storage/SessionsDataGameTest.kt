package pt.isel.ls.storage

import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.toGenre
import pt.isel.ls.data.domain.toName
import pt.isel.ls.exceptions.GameNotFoundException
import pt.isel.ls.storage.mem.SessionsDataMemGame
import kotlin.test.*


class SessionsDataGameTest {

    @Test
    fun createAndReadGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        // Check if the game was created
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(0u)
        // Check the game data
        // Check the game id
        assertEquals(0u, gameData?.id)
        // Check the game name
        assertEquals("game".toName(), gameData?.name)
        // Check the game developer
        assertEquals("developer".toName(), gameData?.developer)
        // Check the game genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), gameData?.genres)
    }

    @Test
    fun readGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // add a game to the storage
        gameStorage.create("game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        // read the game from the storage
        val gameData = gameStorage.getGamesSearch(setOf("RPG".toGenre()), "developer".toName(), 2u, 0u)
        // Check the game data
        // Check if gameData is not null
        assertNotNull(gameData)
        // Check the game id
        assertEquals(0u, gameData[0].id)
        // Check the game name
        assertEquals("game".toName(), gameData[0].name)
        // Check the game developer
        assertEquals("developer".toName(), gameData[0].developer)
        // Check the game genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), gameData[0].genres)
    }

    @Test
    fun updateGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        // Update the game
        // Start by creating a new game object
        val newGame = Game(0u, "newGame".toName(), "newDeveloper".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        gameStorage.update(newGame)
        // Check if the game was updated
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(0u)
        // Check the game data
        // Check the game id
        assertEquals(0u, gameData?.id)
        // Check the game name
        assertEquals("newGame".toName(), gameData?.name)
        // Check the game developer
        assertEquals("newDeveloper".toName(), gameData?.developer)
        // Check the game genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), gameData?.genres)
    }

    @Test
    fun deleteGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        // Delete the game
        gameStorage.delete(0u)
        // Check if the game was deleted
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(0u)
        // Check if the game data is null
        assertNull(gameData)
    }

    @Test
    fun readGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // read the game from the storage
        val gameData = gameStorage.getById(0u)
        // Check if gameData is null
        assertNull(gameData)
    }

    @Test
    fun readGameSearchNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // read the game from the storage
        val gameData = gameStorage.getGamesSearch(setOf("RPG".toGenre()), "developer".toName(), 2u, 0u)
        // Check if gameData is empty
        assertEquals(0, gameData.size)
    }

    @Test
    fun updateGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Update the game
        // Start by creating a new game object
        val newGame = Game(0u, "newGame".toName(), "newDeveloper".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        assertFailsWith<GameNotFoundException> {
            gameStorage.update( newGame)
        }
    }

    @Test
    fun deleteGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Delete the game
        assertFailsWith<GameNotFoundException> {
            gameStorage.delete(0u)
        }
    }

    @Test
    fun isGameNameStoredTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        // Check if the game name is stored
        assertTrue(gameStorage.isGameNameStored("game".toName()))
    }

    @Test
    fun isGenresStoredTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        // Check if the genres are stored
        assertTrue(gameStorage.isGenresStored(setOf("RPG".toGenre(), "Adventure".toGenre())))
    }

    @Test
    fun isDeveloperStoredTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create("game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        // Check if the developer is stored
        assertTrue(gameStorage.isDeveloperStored("developer".toName()))
    }
}