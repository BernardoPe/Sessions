package pt.isel.ls.Storage

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import pt.isel.ls.DTO.Game.Game
import pt.isel.ls.Storage.Mem.SessionsDataMemGame

class SessionsDataGameTest {

    @Test
    fun testCreateAndReadGame() {
        // Create a game
        val game = Game(1, "game", "developer", setOf("genre1", "genre2"))
        // Create a game storage
        val gameStorage: SessionsDataGame = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create(game)
        // Check if the game was created
        // compare the game with the game read from the storage
        assertEquals(game, gameStorage.getById(1))
        // Check the game data
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(1)
        // Check the game id
        assertEquals(1, gameData?.gid)
        // Check the game developer
        assertEquals("developer", gameData?.developer)
        // Check the game genres
        assertEquals(setOf("genre1", "genre2"), gameData?.genres)
        // Check if the game with id 2 was not created
        assertNull(gameStorage.getById(2))
    }

    @Test
    fun testUpdateGame() {
        // Create a game
        val game = Game(1, "game", "developer", setOf("genre1", "genre2"))
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create(game)
        // Update the game
        val newGame = Game(1, "newGame", "newDeveloper", setOf("newGenre1", "newGenre2"))
        gameStorage.update(1, newGame)
        // Check if the game was updated
        // compare the game with the game read from the storage
        assertEquals(newGame, gameStorage.getById(1))
        // Check the game data
        // Start by reading the game from the storage
        val gameData = gameStorage.getById(1)
        // Check the game id
        assertEquals(1, gameData?.gid)
        // Check the game developer
        assertEquals("newDeveloper", gameData?.developer)
        // Check the game genres
        assertEquals(setOf("newGenre1", "newGenre2"), gameData?.genres)
    }

    @Test
    fun testDeleteGame() {
        // Create a game
        val game = Game(1, "game", "developer", setOf("genre1", "genre2"))
        // Create a game storage
        val gameStorage = SessionsDataMemGame()
        // Create the game (add it to the storage)
        gameStorage.create(game)
        // Delete the game
        gameStorage.delete(0)
        // Check if the game was deleted
        assertNull(gameStorage.getById(0))
    }
}