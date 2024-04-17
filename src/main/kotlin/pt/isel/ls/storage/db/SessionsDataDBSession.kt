package pt.isel.ls.storage.db

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.storage.SessionsDataSession
import pt.isel.ls.utils.toTimestamp
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class SessionsDataDBSession : SessionsDataSession {

    private val connectionManager = DBConnectionManager()
    private val connection: Connection get() = connectionManager.getConnection()

    override fun create(session: Session): UInt {
        val statement = connection.prepareStatement(
            "INSERT INTO sessions (game_id, capacity, date) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS,
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
            "SELECT sessions.id as sid, sessions.game_id as gid, date, capacity, games.name as gname, genres, developer, players.id as pid, players.name as pname, email, token_hash FROM sessions " +
                "JOIN games ON sessions.game_id = games.id " +
                "LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id " +
                "LEFT JOIN players ON sessions_players.player_id = players.id " +
                "WHERE sessions.id = ?",
        )

        connection.autoCommit = false
        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        return resultSet.getSessions().firstOrNull().also { statement.close() }
    }

    override fun getSessionsSearch(gid: UInt?, date: LocalDateTime?, state: State?, pid: UInt?, limit: UInt, skip: UInt): Pair<List<Session>, Int> {
        val resQuery = StringBuilder(
            "SELECT sessions.id as sid, sessions.game_id as gid, date, capacity, games.name as gname, genres, developer, players.id as pid, players.name as pname,email, token_hash FROM (" +
                    "SELECT * FROM sessions "
        )
        val countQuery = StringBuilder("SELECT COUNT(*) FROM (SELECT * FROM sessions ")
        val queryParams = mutableListOf<Any>()

        var firstCondition = true

        if (gid != null) {
            resQuery.append("WHERE game_id = ? ")
            countQuery.append("WHERE game_id = ? ")
            queryParams.add(gid.toInt())
            firstCondition = false
        }

        if (date != null) {
            resQuery.append(if (firstCondition) "WHERE date = ? " else "AND date = ? ")
            countQuery.append(if (firstCondition) "WHERE date = ? " else "AND date = ? ")
            queryParams.add(date.toTimestamp())
        }

        if (state != null) {
            val cond = if (state == State.OPEN) "date >= now()" else "date < now()"
            resQuery.append(if (firstCondition) "WHERE $cond " else "AND $cond ")
            countQuery.append(if (firstCondition) "WHERE $cond " else "AND $cond ")
        }

        resQuery.append("Order by id LIMIT ? OFFSET ?) as sessions ")
        countQuery.append(") as sessions ")

        queryParams.add(limit.toInt())
        queryParams.add(skip.toInt())

        resQuery.append(
            "JOIN games ON sessions.game_id = games.id " +
                    "LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id " +
                    "LEFT JOIN players ON sessions_players.player_id = players.id "
        )

        countQuery.append(
            "JOIN games ON sessions.game_id = games.id " +
                    "LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id " +
                    "LEFT JOIN players ON sessions_players.player_id = players.id "
        )

        if (pid != null) {
            resQuery.append("WHERE session_id IN (SELECT session_id FROM sessions_players WHERE player_id = ?) ")
            countQuery.append("WHERE session_id IN (SELECT session_id FROM sessions_players WHERE player_id = ?) ")
            queryParams.add(pid.toInt())
        }

        val statement = connection.prepareStatement(resQuery.toString())
        val countStatement = connection.prepareStatement(countQuery.toString())

        for ((index, param) in queryParams.withIndex()) {
            statement.setObject(index + 1, param)
            if (index < queryParams.size - 2) // dont add limit and skip to count query
                countStatement.setObject(index + 1, param)
        }

        connection.autoCommit = false
        val resultSet = statement.executeQuery()
        val countResultSet = countStatement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        countResultSet.next()

        val total = countResultSet.getInt(1)
        val resultSessions = resultSet.getSessions()

        return resultSessions to total.also { statement.close(); countStatement.close() }
    }

    override fun addPlayer(sid: UInt, player: Player): Boolean {
        // Set the statement to insert a new player in the session
        val statement = connection.prepareStatement(
            "INSERT INTO sessions_players (session_id, player_id) VALUES (?, ?)",
        )

        // Set the parameters
        connection.autoCommit = false
        statement.setInt(1, sid.toInt())
        statement.setInt(2, player.id.toInt())
        // Execute the statement and get the result
        val res = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        // Return the result of the operation
        // It returns true if the player was added to the session
        return res > 0.also { statement.close() }
    }

    override fun removePlayer(sid: UInt, pid: UInt): Boolean {
        // Set the statement to remove a player from the session
        val statement = connection.prepareStatement(
            "DELETE FROM sessions_players WHERE session_id = ? AND player_id = ?",
        )

        connection.autoCommit = false
        // Set the parameters
        statement.setInt(1, sid.toInt())
        statement.setInt(2, pid.toInt())
        // Execute the statement and get the result
        val res = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        // Return the result of the operation
        // It returns true if the player was removed from the session
        return res > 0.also { statement.close() }
    }

    override fun update(sid: UInt, capacity: UInt, date: LocalDateTime): Boolean {
        // Set the statement to update the session
        val query = StringBuilder("UPDATE sessions SET capacity = ?, date = ? WHERE id = ?")
        // Create a list to store the parameters
        // Prepare the statement
        val statement = connection.prepareStatement(query.toString())

        connection.autoCommit = false
        // Set the parameters
        statement.setInt(1, capacity.toInt())
        statement.setTimestamp(2, date.toTimestamp())
        statement.setInt(3, sid.toInt())

        // Execute the statement and get the result
        val res = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        // Return the result of the operation
        return res > 0.also { statement.close() }
    }

    override fun delete(id: UInt): Boolean {
        val statement = connection.prepareStatement(
            "DELETE FROM sessions WHERE id = ?",
        )

        connection.autoCommit = false
        statement.setInt(1, id.toInt())
        val res = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        return res > 0.also { statement.close() }
    }

    private fun ResultSet.getSessions(): List<Session> {
        if (!next()) return emptyList()

        var currSession = this.getSession()

        var sessions = listOf<Session>()

        while (next()) {
            if (getInt("sid") != currSession.id.toInt()) {
                sessions = sessions + currSession
                currSession = getSession()
            }

            if (getObject("pid") != null) {
                currSession = currSession.copy(playersSession = currSession.playersSession + this.getPlayer())
            }
        }

        sessions = sessions + currSession

        return sessions
    }

    private fun ResultSet.getSession(): Session {
        val player = if (getObject("pid") != null) setOf(this.getPlayer()) else emptySet()

        return Session(
            this.getInt("sid").toUInt(),
            this.getInt("capacity").toUInt(),
            this.getTimestamp("date").toLocalDateTime().toKotlinLocalDateTime(),
            this.getGameSession(),
            player,
        )
    }

    private fun ResultSet.getPlayer(): Player {
        return Player(
            this.getInt("pid").toUInt(),
            this.getString("pname").toName(),
            this.getString("email").toEmail(),
            this.getLong("token_hash"),
        )
    }

    private fun ResultSet.getGameSession(): Game {
        var genres = emptySet<Genre>()
        val genreArr = this.getArray("genres").resultSet

        while (genreArr.next()) {
            genres += Genre(genreArr.getString(2))
        }

        return Game(
            this.getInt("gid").toUInt(),
            this.getString("gname").toName(),
            this.getString("developer").toName(),
            genres,
        )
    }

}
