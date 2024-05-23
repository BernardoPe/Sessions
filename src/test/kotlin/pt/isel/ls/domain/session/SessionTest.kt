package pt.isel.ls.domain.session

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.session.SESSION_MAX_CAPACITY
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.data.domain.session.toState
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toGenre
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.utils.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SessionTest {

    @Test
    fun `test successful session creation`() {
        val session = Session(1u, 50u, "2030-03-01T15:00:00".toLocalDateTime(), newGameTest(), newPlayersTest())
        assertEquals(1u, session.id)
        assertEquals(50u, session.capacity)
        assertEquals("2030-03-01T15:00:00".toLocalDateTime(), session.date)
        assertEquals(Game(1u, "Test Game 1".toName(), "Test Developer".toName(), setOf("Adventure".toGenre(), "RPG".toGenre())), session.gameSession)
        assertEquals(
            setOf(
                Player(1u, "player1".toName(), "player1@example.com".toEmail(), uuid),
                Player(2u, "player2".toName(), "player2@example.com".toEmail(), uuid),
                Player(3u, "player3".toName(), "player3@example.com".toEmail(), uuid),
                Player(4u, "player4".toName(), "player4@example.com".toEmail(), uuid),
                Player(5u, "player5".toName(), "player5@example.com".toEmail(), uuid),
                Player(6u, "player6".toName(), "player6@example.com".toEmail(), uuid),
            ),
            session.playersSession,
        )
    }

    @Test
    fun `test session creation with capacity less than 1`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(1u, 0u, "2030-03-01T15:00:00".toLocalDateTime(), newGameTest(), newPlayersTest())
        }
        assertEquals("Session capacity must be at least 1 and at most $SESSION_MAX_CAPACITY", exception.message)
    }

    @Test
    fun `test session creation with capacity greater than max`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(1u, SESSION_MAX_CAPACITY + 1u, "2030-03-01T15:00:00".toLocalDateTime(), newGameTest(), newPlayersTest())
        }
        assertEquals("Session capacity must be at least 1 and at most $SESSION_MAX_CAPACITY", exception.message)
    }

    @Test
    fun `test session creation with players greater than capacity`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Session(1u, 5u, "2030-03-01T15:00:00".toLocalDateTime(), newGameTest(), newPlayersTest())
        }
        assertEquals("Session capacity must not be less than the number of players", exception.message)
    }

   @Test
    fun `test session state when session is open`() {
        val session = Session(1u, 50u, "2030-03-01T15:00:00".toLocalDateTime(), newGameTest(), newPlayersTest())
        assertEquals("OPEN", session.state.toString())
    }

    @Test
    fun `test session state when session is close`() {
        val session = Session(1u, 50u, "2020-03-01T15:00:00".toLocalDateTime(), newGameTest(), newPlayersTest())
        assertEquals("CLOSE", session.state.toString())
    }

    @Test
    fun `test string to state`() {
        assertEquals(State.OPEN, "OPEN".toState())
        assertEquals(State.CLOSE, "CLOSE".toState())
    }

    @Test
    fun `test string to state with invalid state`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            "INVALID".toState()
        }
        assertEquals("Invalid state", exception.message)
    }

    @Test
    fun `test state to string`() {
        assertEquals("OPEN", State.OPEN.toString())
        assertEquals("CLOSE", State.CLOSE.toString())
    }

    companion object {
        fun newGameTest() = Game(
            1u,
            "Test Game 1".toName(),
            "Test Developer".toName(),
            setOf("Adventure".toGenre(), "RPG".toGenre()),
        )

        val uuid = 0L

        fun newPlayersTest() = setOf(
            Player(1u, "player1".toName(), "player1@example.com".toEmail(), uuid),
            Player(2u, "player2".toName(), "player2@example.com".toEmail(), uuid),
            Player(3u, "player3".toName(), "player3@example.com".toEmail(), uuid),
            Player(4u, "player4".toName(), "player4@example.com".toEmail(), uuid),
            Player(5u, "player5".toName(), "player5@example.com".toEmail(), uuid),
            Player(6u, "player6".toName(), "player6@example.com".toEmail(), uuid),
        )
    }
}
