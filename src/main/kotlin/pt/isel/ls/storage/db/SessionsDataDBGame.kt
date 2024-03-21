package pt.isel.ls.storage.db


import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.storage.SessionsDataGame
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class SessionsDataDBGame(private val connection: Connection) : SessionsDataGame {
    override fun create(name: Name, developer: Name, genres: Set<Genre>): UInt {

        val statement = connection.prepareStatement(
            "INSERT INTO games (name, developer, genres) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )

        connection.autoCommit = false
        statement.setString(1, name.toString())
        statement.setString(2, developer.toString())
        statement.setArray(3, connection.createArrayOf("VARCHAR", genres.map { it.toString() }.toTypedArray()))
        statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        val generatedKeys = statement.generatedKeys
        generatedKeys.next()
        return generatedKeys.getInt(1).toUInt().also { statement.close() }
    }

    override fun isGameNameStored(name: Name): Boolean {

        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE name = ?"
        )

        connection.autoCommit = false
        statement.setString(1, name.toString())
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        return resultSet.next().also { statement.close() }
    }

    override fun getGamesSearch(genres: Set<Genre>, developer: Name, limit: UInt, skip: UInt): List<Game> {

        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE genres @> ? AND developer = ? LIMIT ? OFFSET ?"
        )

        connection.autoCommit = false
        statement.setArray(1, connection.createArrayOf("VARCHAR", genres.map { it.toString() }.toTypedArray()))
        statement.setString(2, developer.toString())
        statement.setInt(3, limit.toInt())
        statement.setInt(4, skip.toInt())
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        return resultSet.getGames().also { statement.close() }

    }

    override fun getAllGames(): List<Game> {

        val statement = connection.prepareStatement(
            "SELECT * FROM games"
        )

        connection.autoCommit = false
        val resultSet = statement.executeQuery()
        connection.commit()
        connection.autoCommit = true

        return resultSet.getGames().also { statement.close() }

    }

    override fun getById(id: UInt): Game? {

        val statement = connection.prepareStatement(
            "SELECT * FROM games WHERE id = ?"
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
            "UPDATE games SET name = ?, developer = ?, genres = ? WHERE id = ?"
        )

        connection.autoCommit = false
        statement.setString(1, value.name.toString())
        statement.setString(2, value.developer.toString())
        statement.setArray(3, connection.createArrayOf("VARCHAR(40)", value.genres.map { it.toString() }.toTypedArray()))
        statement.setInt(4, value.id.toInt())
        val updated = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        return updated > 0 .also { statement.close() }
    }

    override fun delete(id: UInt): Boolean {
        val statement = connection.prepareStatement(
            "DELETE FROM games WHERE id = ?"
        )

        connection.autoCommit = false
        statement.setInt(1, id.toInt())
        val deleted = statement.executeUpdate()
        connection.commit()
        connection.autoCommit = true

        return deleted > 0 .also { statement.close() }
    }


    private fun ResultSet.getGames(): List<Game> {
        var games = listOf<Game>()
        while (this.next()) {

            var genres = emptySet<Genre>()
            val genreArr = this.getArray("genres").resultSet

            while (genreArr.next()) {
                genres += Genre(genreArr.getString(2))
            }

            games += Game(
                this.getInt("id").toUInt(),
                Name(this.getString("name")),
                Name(this.getString("developer")),
                genres
            )
        }
        return games
    }

}