package pt.isel.ls.storage.db

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.session.Session
import pt.isel.ls.data.domain.session.State
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.data.mapper.toPasswordHash
import pt.isel.ls.storage.SessionsDataSession
import pt.isel.ls.utils.toTimestamp
import java.sql.ResultSet
import java.sql.Statement

class SessionsDataDBSession(dbURL: String) : SessionsDataSession, DBManager(dbURL) {

    override fun create(capacity: UInt, date: LocalDateTime, gid: UInt): UInt = execQuery { connection ->

        val statement = connection.prepareStatement(
            "INSERT INTO sessions (game_id, capacity, date) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS,
        )

        statement.setInt(1, gid.toInt())
        statement.setInt(2, capacity.toInt())
        statement.setTimestamp(3, date.toTimestamp())
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        generatedKeys.next()
        generatedKeys.getInt(1).toUInt().also { statement.close() }
    } as UInt

    override fun getById(id: UInt): Session? = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT sessions.id as sid, sessions.game_id as gid, date, capacity, games.name as gname, genres, developer, players.id as pid, players.name as pname, email, password_hash FROM sessions " +
                "JOIN games ON sessions.game_id = games.id " +
                "LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id " +
                "LEFT JOIN players ON sessions_players.player_id = players.id " +
                "WHERE sessions.id = ?",
        )

        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()

        resultSet.getSessions().firstOrNull().also { statement.close() }
    } as Session?

    @Suppress("UNCHECKED_CAST")
    override fun getSessionsSearch(
        gid: UInt?,
        date: LocalDateTime?,
        state: State?,
        pid: UInt?,
        limit: UInt,
        skip: UInt
    ): Pair<List<Session>, Int> =
        execQuery { connection ->
            val resQuery = StringBuilder(
                "SELECT DISTINCT sessions.id FROM sessions "
            )
            val countQuery = StringBuilder("SELECT COUNT(*) FROM (SELECT DISTINCT sessions.id FROM sessions ")
            val queryParams = mutableListOf<Any>()

            val searchQuery = StringBuilder()

            searchQuery.append(
                "JOIN games ON sessions.game_id = games.id " +
                        "LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id " +
                        "LEFT JOIN players ON sessions_players.player_id = players.id "
            )

            var firstCondition = true

            if (gid != null) {
                searchQuery.append("WHERE games.id = ? ")
                queryParams.add(gid.toInt())
                firstCondition = false
            }

            if (date != null) {
                searchQuery.append(if (firstCondition) "WHERE date = ? " else "AND date = ? ")
                queryParams.add(date.toTimestamp())
                firstCondition = false
            }

            if (state != null) {
                val cond = if (state == State.OPEN) "date >= now()" else "date < now()"
                searchQuery.append(if (firstCondition) "WHERE $cond " else "AND $cond ")
                firstCondition = false
            }

            if (pid != null) {
                searchQuery.append(if (firstCondition) "WHERE players.id = ? " else "AND players.id = ? ")
                queryParams.add(pid.toInt())
            }

            resQuery.append(searchQuery)
            countQuery.append(searchQuery)

            countQuery.append(") as sessions ")
            resQuery.append("ORDER BY sessions.id LIMIT ? OFFSET ?")

            queryParams.add(limit.toInt())
            queryParams.add(skip.toInt())

            val statement = connection.prepareStatement(resQuery.toString())
            val countStatement = connection.prepareStatement(countQuery.toString())

            for ((index, param) in queryParams.withIndex()) {
                statement.setObject(index + 1, param)
                if (index < queryParams.size - 2) // dont add limit and skip to count query
                    countStatement.setObject(index + 1, param)
            }

            val resultSet = statement.executeQuery()
            val countResultSet = countStatement.executeQuery()
            val sessionStmt = connection.prepareStatement(
                "SELECT sessions.id as sid, sessions.game_id as gid, date, capacity, games.name as gname, genres, developer, players.id as pid, players.name as pname, email, password_hash FROM sessions " +
                        "JOIN games ON sessions.game_id = games.id " +
                        "LEFT JOIN sessions_players ON sessions.id = sessions_players.session_id " +
                        "LEFT JOIN players ON sessions_players.player_id = players.id " +
                        "WHERE sessions.id = ?",
            )
            val resultSessions = mutableListOf<Session>()
            while (resultSet.next()) {
                sessionStmt.setInt(1, resultSet.getInt("id"))
                val sessionResultSet = sessionStmt.executeQuery()
                resultSessions.add(sessionResultSet.getSessions().first())
            }
            countResultSet.next()

            val total = countResultSet.getInt(1)

            resultSessions to total.also { statement.close(); countStatement.close(); sessionStmt.close() }
        } as Pair<List<Session>, Int>

    override fun addPlayer(sid: UInt, pid: UInt): Boolean = execQuery { connection ->
        // Check if the player is already in the session
        // Set the statement to insert a new player in the session
        val statement = connection.prepareStatement(
            "INSERT INTO sessions_players (session_id, player_id) VALUES (?, ?)",
        )

        // Set the parameters
        statement.setInt(1, sid.toInt())
        statement.setInt(2, pid.toInt())
        // Execute the statement and get the result
        val res = statement.executeUpdate()

        // Return the result of the operation
        // It returns true if the player was added to the session
        res > 0.also { statement.close() }
    } as Boolean

    override fun removePlayer(sid: UInt, pid: UInt): Boolean = execQuery { connection ->
        // Set the statement to remove a player from the session
        val statement = connection.prepareStatement(
            "DELETE FROM sessions_players WHERE session_id = ? AND player_id = ?",
        )
        // Set the parameters
        statement.setInt(1, sid.toInt())
        statement.setInt(2, pid.toInt())
        // Execute the statement and get the result
        val res = statement.executeUpdate()
        // Return the result of the operation
        // It returns true if the player was removed from the session
        res > 0.also { statement.close() }
    } as Boolean

    override fun update(sid: UInt, capacity: UInt?, date: LocalDateTime?): Boolean = execQuery { connection ->
        val query = StringBuilder("UPDATE sessions SET ")
        val queryParams = mutableListOf<Any>()

        if (capacity != null) {
            query.append("capacity = ?, ")
            queryParams.add(capacity.toInt())
        }

        if (date != null) {
            query.append("date = ?, ")
            queryParams.add(date.toTimestamp())
        }

        query.delete(query.length - 2, query.length)

        query.append(" WHERE id = ?")
        queryParams.add(sid.toInt())

        val statement = connection.prepareStatement(query.toString())

        for ((index, param) in queryParams.withIndex()) {
            statement.setObject(index + 1, param)
        }

        val res = statement.executeUpdate()

        res > 0.also { statement.close() }
    } as Boolean

    override fun delete(id: UInt): Boolean = execQuery { connection ->
        val statement = connection.prepareStatement(
            "DELETE FROM sessions WHERE id = ?",
        )

        statement.setInt(1, id.toInt())
        val res = statement.executeUpdate()

        res > 0.also { statement.close() }
    } as Boolean

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
            this.getString("password_hash").toPasswordHash()
        )
    }

    private fun ResultSet.getGameSession(): Game {
        val genres = mutableSetOf<Genre>()
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
