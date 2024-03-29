package pt.isel.ls.domain.game

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.toGenre
import pt.isel.ls.data.domain.toName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameTest {

    @Test
    fun `test successful game creation`() {
        val game = Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        assertEquals(1u, game.id)
        assertEquals("Test Game".toName(), game.name)
        assertEquals("Test Developer".toName(), game.developer)
        assertEquals(setOf("RPG".toGenre(), "Adventure".toGenre()), game.genres)
    }

    @Test
    fun `test game creation with empty name`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Game(1u, "".toName(), "Test Developer".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        }
        assertEquals("The name must not be empty", exception.message)
    }

    @Test
    fun `test game creation with empty developer`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Game(1u, "Test Game".toName(), "".toName(), setOf("RPG".toGenre(), "Adventure".toGenre()))
        }
        assertEquals("The name must not be empty", exception.message)
    }

    @Test
    fun `test game creation with empty genres`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Game(1u, "Test Game".toName(), "Test Developer".toName(), setOf("".toGenre(), "".toGenre()))
        }
        assertEquals("Genres must not be blank", exception.message)
    }
}
