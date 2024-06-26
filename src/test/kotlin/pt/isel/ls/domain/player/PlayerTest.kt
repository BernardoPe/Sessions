package pt.isel.ls.domain.player

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlayerTest {

    @Test
    fun `Player creation should succeed when all parameters are valid`() {
        val player = Player(1u, "Test Player".toName(), "testplayer@example.com".toEmail(), PasswordHash("TestPassword123#"))
        assertEquals(1u, player.id)
        assertEquals("Test Player".toName(), player.name)
        assertEquals("testplayer@example.com".toEmail(), player.email)
        assertEquals(PasswordHash("TestPassword123#"), player.password)
    }

    @Test
    fun `Player creation should fail when name is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Player(1u, "".toName(), "testplayer@example.com".toEmail(), PasswordHash("TestPassword123#"))
        }
    }

    @Test
    fun `Player creation should fail when email is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Player(1u, "Test Player".toName(), "".toEmail(), PasswordHash("TestPassword123#"))
        }
    }
}
