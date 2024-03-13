package pt.isel.ls.DTO.Game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameTest {

    @Test
    fun `test successful game creation`() {
        val game = Game(1, "Test Game", "Test Developer", listOf("Genre1", "Genre2"))
        assertEquals(1, game.gid)
        assertEquals("Test Game", game.name)
        assertEquals("Test Developer", game.developer)
        assertEquals(listOf("Genre1", "Genre2"), game.genres)
    }

    @Test
    fun `test game creation with negative gid`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Game(-1, "Test Game", "Test Developer", listOf("Genre1", "Genre2"))
        }
        assertEquals("The game identifier must be a positive integer", exception.message)
    }

    @Test
    fun `test game creation with empty name`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Game(1, "", "Test Developer", listOf("Genre1", "Genre2"))
        }
        assertEquals("The game name must not be empty", exception.message)
    }

    @Test
    fun `test game creation with empty developer`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Game(1, "Test Game", "", listOf("Genre1", "Genre2"))
        }
        assertEquals("The game developer must not be empty", exception.message)
    }

    @Test
    fun `test game creation with empty genres`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Game(1, "Test Game", "Test Developer", emptyList())
        }
        assertEquals("The game genres must not be empty", exception.message)
    }
}