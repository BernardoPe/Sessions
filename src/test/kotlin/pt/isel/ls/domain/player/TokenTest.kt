package pt.isel.ls.domain.player

import pt.isel.ls.data.domain.player.Token
import pt.isel.ls.utils.currentLocalTime
import pt.isel.ls.utils.plus
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days

class TokenTest {

    @Test
    fun `Token creation should succeed`() {
        val token = UUID.randomUUID()
        val timeCreation = currentLocalTime()
        val timeExpiration = currentLocalTime()
        val createdToken = Token(token, 1u, timeCreation, timeExpiration)
        assertEquals(token, createdToken.token)
        assertEquals(1u, createdToken.playerId)
        assertEquals(timeCreation, createdToken.timeCreation)
        assertEquals(timeExpiration, createdToken.timeExpiration)
    }

    @Test
    fun `Token isExpired should return false`() {
        val token = UUID.randomUUID()
        val timeCreation = currentLocalTime()
        val timeExpiration = currentLocalTime() + 1.days
        val createdToken = Token(token, 1u, timeCreation, timeExpiration)
        assertEquals(false, createdToken.isExpired())
    }

    @Test
    fun `Token isExpired should return true`() {
        val token = UUID.randomUUID()
        val timeCreation = currentLocalTime()
        val timeExpiration = currentLocalTime().plus((-1).days)
        val createdToken = Token(token, 1u, timeCreation, timeExpiration)
        assertEquals(true, createdToken.isExpired())
    }

}