package pt.isel.ls.domain.session

import pt.isel.ls.domain.game.Game
import pt.isel.ls.domain.player.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SessionTest {

    @Test
    fun `test successful session creation`() {
        val session = Session(1, 50, "2023-03-01 15:00:00", newGameTest(), newPlayersTest())
        assertEquals(1, session.sid)
        assertEquals(50, session.capacity)
        assertEquals("2023-03-01 15:00:00", session.date)
        assertEquals(Game(1, "Test Game 1", "Test Developer", setOf("Genre1", "Genre2")), session.gameSession)
        assertEquals(
            setOf(
                Player(1, "player1", "player1@example.com"),
                Player(2, "player2", "player2@example.com"),
                Player(3, "player3", "player3@example.com"),
                Player(4, "player4", "player4@example.com"),
                Player(5, "player5", "player5@example.com"),
                Player(6, "player6", "player6@example.com"),
            ), session.playersSession
        )

    }

    @Test
    fun `test session creation with negative ssid`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(-1, 50, "2023-03-01", newGameTest(), newPlayersTest())
        }
        assertEquals("Session identifier must be a positive number", exception.message)
    }

    @Test
    fun `test session creation with capacity less than 1`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(1, 0, "2023-03-01", newGameTest(), newPlayersTest())
        }
        assertEquals("Session capacity must be a positive number", exception.message)
    }

    @Test
    fun `test session creation with capacity greater than max`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(1, SESSION_MAX_CAPACITY + 1, "2023-03-01", newGameTest(), newPlayersTest())
        }
        assertEquals("Session capacity must be less than or equal to $SESSION_MAX_CAPACITY", exception.message)
    }

    companion object {
        fun newGameTest() = Game(
            1,
            "Test Game 1",
            "Test Developer",
            setOf("Genre1", "Genre2")
        )

        fun newPlayersTest() = setOf(
            Player(1, "player1", "player1@example.com"),
            Player(2, "player2", "player2@example.com"),
            Player(3, "player3", "player3@example.com"),
            Player(4, "player4", "player4@example.com"),
            Player(5, "player5", "player5@example.com"),
            Player(6, "player6", "player6@example.com"),
        )
    }
}