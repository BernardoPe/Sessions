package pt.isel.ls.storage.db

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.domain.util.Name
import pt.isel.ls.storage.SessionsDataGame
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class SessionsDataDBGame : SessionsDataGame {

    private val connManager: DBConnectionManager = DBConnectionManager()
    private val connection: Connection get() = connManager.getConnection()

    override fun create(game: Game): UInt {

        val statement = connection.prepareStatement(
            "INSERT INTO games (name, developer, genres) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS,
        )
        connection.autoCommit = false
        statement.setString(1, game.name.toString())
        statement.setString(2, game.developer.toString())
        statement.setArray(3, connection.createArrayOf("VARCHAR", game.genres.map { it.toString() }.toTypedArray()))
        statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        val generatedKeys = statement.generatedKeys
        generatedKeys.next()
        return generatedKeys.getInt(1).toUInt().also { statement.close(); connection.close() }
    }

    override fun isGameNameStored(name: Name): Boolean {
        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE name = ?",
        )

        connection.autoCommit = false
        statement.setString(1, name.toString())
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        return resultSet.next().also { statement.close() }
    }

    override fun getGamesSearch(genres: Set<Genre>?, developer: Name?, limit: UInt, skip: UInt): Pair<List<Game>, Int> {

        var resQuery = "SELECT * FROM games "
        var countQuery = "SELECT COUNT(*) FROM games "
        val params = mutableListOf<Any>()

        var searchQuery = ""
        if (genres != null || developer != null) {
            searchQuery += "WHERE "
            if (genres != null) {
                searchQuery += "genres @> ? "
                params.add(connection.createArrayOf("VARCHAR", genres.map { it.toString() }.toTypedArray()))
            }
            if (developer != null) {
                if (genres != null) {
                     searchQuery += "AND "
                }
                searchQuery += "developer = ? "
                params.add(developer.toString())
            }
        }

        countQuery += searchQuery
        resQuery += searchQuery

        params.add(limit.toInt())
        params.add(skip.toInt())

        resQuery += "Order by id LIMIT ? OFFSET ?"

        val statement = connection.prepareStatement(resQuery)
        val countStatement = connection.prepareStatement(countQuery)

        params.forEachIndexed { index, param ->
            statement.setObject(index + 1, param)
            if (index < params.size - 2) // dont add limit and skip to count query
                countStatement.setObject(index + 1, param)
        }

        connection.autoCommit = false
        val resultSet = statement.executeQuery()
        val countResultSet = countStatement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        countResultSet.next()
        val total = countResultSet.getInt(1)

        return Pair(resultSet.getGames(), total).also { statement.close(); countStatement.close() }
    }

    override fun getAllGames(): List<Game> {
        val statement = connection.prepareStatement(
            "SELECT * FROM games",
        )
        connection.autoCommit = false
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        return resultSet.getGames().also { statement.close() }
    }

    override fun getById(id: UInt): Game? {
        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE id = ?",
        )

        connection.autoCommit = false
        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        return resultSet.getGames().firstOrNull().also { statement.close() }
    }

    override fun update(value: Game): Boolean {
        val statement = connection.prepareStatement(
            "UPDATE games SET name = ?, developer = ?, genres = ? WHERE id = ?",
        )

        connection.autoCommit = false
        statement.setString(1, value.name.toString())
        statement.setString(2, value.developer.toString())
        statement.setArray(3, connection.createArrayOf("VARCHAR", value.genres.map { it.toString() }.toTypedArray()))
        statement.setInt(4, value.id.toInt())
        val updated = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        return updated > 0.also { statement.close() }
    }

    override fun delete(id: UInt): Boolean {
        val statement = connection.prepareStatement(
            "DELETE FROM games WHERE id = ?",
        )

        connection.autoCommit = false
        statement.setInt(1, id.toInt())
        val deleted = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        return deleted > 0.also { statement.close() }
    }

    private fun ResultSet.getGames(): List<Game> {
        var games = listOf<Game>()
        while (this.next()) {
            var genres = emptySet<Genre>()
            val genreArr = this.getArray("genres").resultSet

            while (genreArr.next()) {
                val genreStr = genreArr.getString(2)
                genres += Genre(genreStr)
            }

            games += Game(
                this.getInt("id").toUInt(),
                Name(this.getString("name")),
                Name(this.getString("developer")),
                genres,
            )
        }
        return games
    }

}
