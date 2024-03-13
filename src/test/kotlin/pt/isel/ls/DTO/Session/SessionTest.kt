package pt.isel.ls.DTO.Session

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SessionTest {

    @Test
    fun `test successful session creation`() {
        val session = Session(1, 50, 1, "2023-03-01")
        assertEquals(1, session.ssid)
        assertEquals(50, session.capacity)
        assertEquals(1, session.gid)
        assertEquals("2023-03-01", session.date)
    }

    @Test
    fun `test session creation with negative ssid`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(-1, 50, 1, "2023-03-01")
        }
        assertEquals("Session identifier must be a positive number", exception.message)
    }

    @Test
    fun `test session creation with capacity less than 1`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(1, 0, 1, "2023-03-01")
        }
        assertEquals("Session capacity must be a positive number", exception.message)
    }

    @Test
    fun `test session creation with capacity greater than max`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(1, SESSION_MAX_CAPACITY + 1, 1, "2023-03-01")
        }
        assertEquals("Session capacity must be less than or equal to $SESSION_MAX_CAPACITY", exception.message)
    }

    @Test
    fun `test session creation with negative gid`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(1, 50, -1, "2023-03-01")
        }
        assertEquals("Game identifier must be a positive number", exception.message)
    }
}