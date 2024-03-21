package pt.isel.ls.storage.db

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.data.domain.toEmail
import pt.isel.ls.data.domain.toName
import pt.isel.ls.storage.SessionsDataSession
import pt.isel.ls.utils.toTimestamp
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Timestamp

class SessionsDataDBSession(private val connection: Connection): SessionsDataSession {
    override fun create(session: Session): UInt {
        val statement = connection.prepareStatement(
            "INSERT INTO sessions (game_id, capacity, date) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )

        connection.autoCommit = false
        statement.setInt(1, session.gameSession.id.toInt())
        statement.setInt(2, session.capacity.toInt())
        statement.setTimestamp(3, session.date.toTimestamp())
        statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        val generatedKeys = statement.generatedKeys
        generatedKeys.next()
        return generatedKeys.getInt(1).toUInt().also { statement.close() }
    }

    override fun getById(id: UInt): Session? {
        val statement = connection.prepareStatement(
            "SELECT * FROM sessions WHERE id = ?"
        )

        connection.autoCommit = false
        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        return resultSet.getSessions().firstOrNull().also { statement.close() }
    }

    override fun getSessionsSearch(gid: UInt, date: LocalDateTime?, state: State?, pid: UInt?, limit: UInt, skip: UInt): List<Session> {

        val query = StringBuilder("SELECT * FROM sessions WHERE game_id = ?")
        val queryParams = mutableListOf<Any>(gid.toInt())

        if (date != null) {
            query.append(" AND date = ?")
            queryParams.add(date.toTimestamp())
        }

        if (pid != null) {
            query.append(" AND id IN (SELECT session_id FROM sessions_players WHERE player_id = ?)")
            queryParams.add(pid.toInt())
        }

        query.append(" LIMIT ? OFFSET ?")

        val statement = connection.prepareStatement(query.toString())

        connection.autoCommit = false

        for ((index, param) in queryParams.withIndex()) {
            statement.setObject(index + 1, param)
        }

        statement.setInt(queryParams.size + 1, limit.toInt())
        statement.setInt(queryParams.size + 2, skip.toInt())

        val resultSet = statement.executeQuery()

        connection.commit()
        connection.autoCommit = true

        return if (state != null) {
            resultSet.getSessions().filter { it.state == state }.also { statement.close() }
        } else {
            resultSet.getSessions().also { statement.close() }
        }

    }

    override fun update(sid: UInt, player: Player): Boolean {
        val statement = connection.prepareStatement(
            "INSERT INTO sessions_players (session_id, player_id) VALUES (?, ?)"
        )

        connection.autoCommit = false
        statement.setInt(1, sid.toInt())
        statement.setInt(2, player.id.toInt())
        val res = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        return res > 0 .also { statement.close() }
    }

    override fun delete(id: UInt): Boolean {
        val statement = connection.prepareStatement(
            "DELETE FROM sessions WHERE id = ?"
        )

        connection.autoCommit = false
        statement.setInt(1, id.toInt())
        val res = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        return res > 0 .also { statement.close() }
    }

    private fun ResultSet.getSessions(): List<Session> {
        val sessions = mutableListOf<Session>()
        while (this.next()) {
            val id = getInt("id").toUInt()
            val capacity = getInt("capacity").toUInt()
            val game = getGameSession(getInt("game_id").toUInt())
            val date = getTimestamp("date").toLocalDateTime().toKotlinLocalDateTime()
            sessions.add(Session(id,capacity,date,game,getPlayersInSession(id).toSet()))
        }
        return sessions
    }

    private fun getGameSession(gid: UInt): Game {
        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE id = ?"
        )

        connection.autoCommit = false
        statement.setInt(1, gid.toInt())
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        resultSet.next()

        var genres = emptySet<Genre>()
        val genreArr = resultSet.getArray("genres").resultSet

        while (genreArr.next()) {
            genres += Genre(genreArr.getString(2))
        }

        return Game(
            resultSet.getInt("id").toUInt(),
            resultSet.getString("name").toName(),
            resultSet.getString("developer").toName(),
            genres
        )

    }

    private fun getPlayersInSession(sid: UInt) : List<Player> {
        val statement = connection.prepareStatement(
            "SELECT * FROM sessions_players WHERE session_id = ?"
        )

        connection.autoCommit = false
        statement.setInt(1, sid.toInt())
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        var playerIds = listOf<UInt>()

        while (resultSet.next()) {
            playerIds += resultSet.getInt("player_id").toUInt()
        }

        return playerIds.getPlayers()
    }

    private fun List<UInt>.getPlayers(): List<Player> {

        var players = listOf<Player>()

        val statement = connection.prepareStatement(
            "SELECT * FROM players WHERE id = ANY(?)"
        )

        connection.autoCommit = false
        statement.setArray(1, connection.createArrayOf("INTEGER", this.map { it.toInt() }.toTypedArray()))
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        while (resultSet.next()) {
            players += Player(
                resultSet.getInt("id").toUInt(),
                resultSet.getString("name").toName(),
                resultSet.getString("email").toEmail(),
                resultSet.getLong("token_hash")
            )
        }

        return players

    }

}