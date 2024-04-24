package pt.isel.ls.storage.db

import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.util.Genre
import pt.isel.ls.data.domain.util.Name
import pt.isel.ls.storage.SessionsDataGame
import java.sql.ResultSet
import java.sql.Statement

class SessionsDataDBGame : SessionsDataGame, DBManager() {
    override fun create(game: Game): UInt = execQuery { connection ->
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
        generatedKeys.getInt(1).toUInt().also { statement.close() }
    } as UInt

    override fun isGameNameStored(name: Name): Boolean = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE name = ?",
        )

        statement.setString(1, name.toString())
        val resultSet = statement.executeQuery()

        resultSet.next().also { statement.close() }
    } as Boolean

    @Suppress("UNCHECKED_CAST")
    override fun getGamesSearch(genres: Set<Genre>?, developer: Name?, limit: UInt, skip: UInt): Pair<List<Game>, Int> =
        execQuery { connection ->

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

            val resultSet = statement.executeQuery()
            val countResultSet = countStatement.executeQuery()

            countResultSet.next()
            val total = countResultSet.getInt(1)

            Pair(resultSet.getGames(), total).also { statement.close(); countStatement.close() }

        }  as Pair<List<Game>, Int>




    @Suppress("UNCHECKED_CAST")
    override fun getAllGames(): List<Game> = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * FROM games",
        )
        val resultSet = statement.executeQuery()

        resultSet.getGames().also { statement.close() }
    } as List<Game>

    override fun getById(id: UInt): Game? = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE id = ?",
        )

        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()

        resultSet.getGames().firstOrNull().also { statement.close() }

    } as Game?

    override fun update(value: Game): Boolean = execQuery { connection ->
        val statement = connection.prepareStatement(
            "UPDATE games SET name = ?, developer = ?, genres = ? WHERE id = ?",
        )

        statement.setString(1, value.name.toString())
        statement.setString(2, value.developer.toString())
        statement.setArray(3, connection.createArrayOf("VARCHAR", value.genres.map { it.toString() }.toTypedArray()))
        statement.setInt(4, value.id.toInt())
        val updated = statement.executeUpdate()

        updated > 0.also { statement.close() }
    } as Boolean

    override fun delete(id: UInt): Boolean = execQuery { connection ->
        val statement = connection.prepareStatement(
            "DELETE FROM games WHERE id = ?",
        )

        statement.setInt(1, id.toInt())
        val deleted = statement.executeUpdate()

        deleted > 0.also { statement.close() }
    } as Boolean

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
