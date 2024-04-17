package pt.isel.ls.storage

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.mapper.toGenre
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.storage.mem.SessionsDataMemGame
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SessionsDataGameTest {

    @Test
    fun createAndReadGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        val game = Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        gameStorage.create(game)
        // Check if the game was created
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(1u)
        // Check the game data
        // Check the game id
        assertEquals(1u, gameData?.id)
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
        val game = Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        gameStorage.create(game)
        // read the game from the storage
        val gameData = gameStorage.getGamesSearch(setOf("RPG".toGenre()), "developer".toName(), 2u, 0u)
        // Check the game data
        // Check if gameData is not null
        assertNotNull(gameData)
        // Check the game id
        assertEquals(1u, gameData.first[0].id)
        // Check the game name
        assertEquals("game".toName(), gameData.first[0].name)
        // Check the game developer
        assertEquals("developer".toName(), gameData.first[0].developer)
        // Check the game genres
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), gameData.first[0].genres)

        assertEquals(1, gameData.second)
    }

    @Test
    fun updateGameTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        val game = Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        gameStorage.create(game)
        // Update the game
        // Start by creating a new game object
        val newGame = Game(1u, "newGame".toName(), "newDeveloper".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        gameStorage.update(newGame)
        // Check if the game was updated
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(1u)
        // Check the game data
        // Check the game id
        assertEquals(1u, gameData?.id)
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
        val game = Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        gameStorage.create(game)
        // Delete the game
        gameStorage.delete(1u)
        // Check if the game was deleted
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(1u)
        // Check if the game data is null
        assertNull(gameData)
    }

    @Test
    fun readGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // read the game from the storage
        val gameData = gameStorage.getById(1u)
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
        assertEquals(0, gameData.first.size)
        assertEquals(0, gameData.second)
    }

    @Test
    fun updateGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Update the game
        // Start by creating a new game object
        val newGame = Game(1u, "newGame".toName(), "newDeveloper".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        assertFalse { gameStorage.update(newGame) }
    }

    @Test
    fun deleteGameNotFoundTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Delete the game
        assertFalse { gameStorage.delete(0u) }
    }

    @Test
    fun isGameNameStoredTest() {
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        val game = Game(0u, "game".toName(), "developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        gameStorage.create(game)
        // Check if the game name is stored
        assertTrue(gameStorage.isGameNameStored("game".toName()))
    }
}
