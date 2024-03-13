package pt.isel.ls.DTO.Player

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerTest {

    @Test
    fun `Player creation should succeed when all parameters are valid`() {
        val player = Player(1, "Test Player", "testplayer@example.com", "token123")
        assertEquals(1, player.pid)
        assertEquals("Test Player", player.name)
        assertEquals("testplayer@example.com", player.email)
        assertEquals("token123", player.token)
    }

    @Test
    fun `Player creation should fail when pid is negative`() {
        assertFailsWith<IllegalArgumentException> {
            Player(-1, "Test Player", "", "token123")
        }
    }

    @Test
    fun `Player creation should fail when name is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Player(1, "", "testplayer@example.com", "token123")
        }
    }

    @Test
    fun `Player creation should fail when email is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Player(1, "Test Player", "", "token123")
        }
    }

    @Test
    fun `Player creation should fail when token is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Player(1, "Test Player", "testplayer@example.com", "")
        }
    }
}