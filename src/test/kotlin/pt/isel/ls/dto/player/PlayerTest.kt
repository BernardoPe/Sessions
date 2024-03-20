package pt.isel.ls.dto.player

import pt.isel.ls.data.domain.player.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerTest {

    @Test
    fun `Player creation should succeed when all parameters are valid`() {
        val player = Player(1, "Test Player", "testplayer@example.com", 0L)
        assertEquals(1, player.pid)
        assertEquals("Test Player", player.name)
        assertEquals("testplayer@example.com", player.email)
    }

    @Test
    fun `Player creation should fail when pid is negative`() {
        assertFailsWith<IllegalArgumentException> {
            Player(-1, "Test Player", "", 0L)
        }
    }

    @Test
    fun `Player creation should fail when name is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Player(1, "", "testplayer@example.com", 0L)
        }
    }

    @Test
    fun `Player creation should fail when email is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Player(1, "Test Player", "", 0L)
        }
    }
}