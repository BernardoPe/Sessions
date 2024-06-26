package pt.isel.ls.storage.db

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.primitives.Genre
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.storage.SessionsDataGame
import java.sql.ResultSet
import java.sql.Statement

class SessionsDataDBGame(private val dbURL: String) : SessionsDataGame {

    private val connection get() = TransactionManager.getConnection(dbURL)

    override fun create(game: Game): UInt {
        val statement = connection.prepareStatement(
            "INSERT INTO games (name, developer, genres) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS,
        )

        statement.setString(1, game.name.toString())
        statement.setString(2, game.developer.toString())
        statement.setArray(3, connection.createArrayOf("VARCHAR", game.genres.map { it.toString() }.toTypedArray()))
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        generatedKeys.next()
        return generatedKeys.getInt(1).toUInt().also { statement.close() }
    }

    override fun getGamesSearch(
        genres: Set<Genre>?,
        developer: Name?,
        name: Name?,
        limit: UInt,
        skip: UInt
    ): Pair<List<Game>, Int> {
            var resQuery = "SELECT * FROM games "
            var countQuery = "SELECT COUNT(*) FROM games "
            val params = mutableListOf<Any>()

            var searchQuery = ""
            var firstCondition = true

            if (genres != null) {
                searchQuery += "WHERE genres @> ? "
                params.add(connection.createArrayOf("VARCHAR", genres.map { it.toString() }.toTypedArray()))
                firstCondition = false
            }

            if (developer != null) {
                searchQuery += if (firstCondition) "WHERE developer = ? " else "AND developer = ? "
                params.add(developer.toString())
                firstCondition = false
            }

            if (name != null) {
                searchQuery += if (firstCondition) "WHERE lower(name) LIKE ? " else "AND lower(name) LIKE ? "
                params.add("${name.name.lowercase()}%")
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

            val resultSet = statement.executeQuery()
            val countResultSet = countStatement.executeQuery()

            countResultSet.next()
            val total = countResultSet.getInt(1)

            return Pair(resultSet.getGames(), total).also { statement.close(); countStatement.close() }
        }

    override fun isGameNameStored(name: Name): Boolean {
        val statement = connection.prepareStatement(
            "SELECT 1 FROM games WHERE name = ?",
        )

        statement.setString(1, name.toString())
        val resultSet = statement.executeQuery()

        return resultSet.next().also { statement.close() }
    }


    override fun getAllGames(): List<Game> {
        val statement = connection.prepareStatement(
            "SELECT * FROM games",
        )
        val resultSet = statement.executeQuery()

        return resultSet.getGames().also { statement.close() }
    }

    override fun getById(id: UInt): Game? {
        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE id = ?",
        )

        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()

        return resultSet.getGames().firstOrNull().also { statement.close() }
    }

    override fun update(value: Game): Boolean {
        val statement = connection.prepareStatement(
            "UPDATE games SET name = ?, developer = ?, genres = ? WHERE id = ?",
        )

        statement.setString(1, value.name.toString())
        statement.setString(2, value.developer.toString())
        statement.setArray(3, connection.createArrayOf("VARCHAR", value.genres.map { it.toString() }.toTypedArray()))
        statement.setInt(4, value.id.toInt())
        val updated = statement.executeUpdate()

        return updated > 0.also { statement.close() }
    }

    override fun delete(id: UInt): Boolean {
        val statement = connection.prepareStatement(
            "DELETE FROM games WHERE id = ?",
        )

        statement.setInt(1, id.toInt())
        val deleted = statement.executeUpdate()

        return deleted > 0.also { statement.close() }
    }

    private fun ResultSet.getGames(): List<Game> {
        val games = mutableListOf<Game>()
        while (this.next()) {
            val genres = mutableSetOf<Genre>()
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
