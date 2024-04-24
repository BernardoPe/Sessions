package pt.isel.ls.storage.db

import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.util.Email
import pt.isel.ls.data.domain.util.Name
import pt.isel.ls.storage.SessionsDataPlayer
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class SessionsDataDBPlayer : SessionsDataPlayer, DBManager() {

    @Suppress("UNCHECKED_CAST")
    override fun create(player: Player): Pair<UInt, UUID> = execQuery { connection ->
        val statement = connection.prepareStatement(
            "INSERT INTO players (name, email,token_hash) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS,
        )

        val token = UUID.randomUUID()

        statement.setString(1, player.name.toString())
        statement.setString(2, player.email.toString())
        statement.setLong(3, token.hash())
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        generatedKeys.next()
        Pair(generatedKeys.getInt(1).toUInt(), token)

    } as Pair<UInt, UUID>

    override fun getById(id: UInt): Player? = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * FROM players WHERE id = ?",
        )
        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()

        resultSet.getPlayers().firstOrNull().also { statement.close() }
    } as Player?

    override fun isEmailStored(email: Email): Boolean = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT 1 FROM players WHERE email = ?",
        )

        statement.setString(1, email.toString())
        val resultSet = statement.executeQuery()

        resultSet.next().also { statement.close() }
    } as Boolean

    override fun isNameStored(name: Name): Boolean = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT 1 FROM players WHERE name = ?",
        )

        statement.setString(1, name.toString())
        val resultSet = statement.executeQuery()

        resultSet.next().also { statement.close() }
    } as Boolean

    @Suppress("UNCHECKED_CAST")
    override fun getAll(): List<Player> = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * FROM players",
        )

        val resultSet = statement.executeQuery()

        resultSet.getPlayers().also { statement.close() }
    } as List<Player>

    @Suppress("UNCHECKED_CAST")
    override fun getPlayersSearch(name: Name?, limit: UInt, skip: UInt): Pair<List<Player>, Int> = execQuery { connection ->

        val searchQuery = StringBuilder(
            "SELECT * FROM players ",
        )

        val countQuery = StringBuilder(
            "SELECT COUNT(*) FROM players ",
        )

        val queryParams = mutableListOf<Any>()

        if (name != null) {
            searchQuery.append("WHERE name LIKE ? ")
            countQuery.append("WHERE name LIKE ? ")
            queryParams.add("${name.name}%")
        }

        searchQuery.append("ORDER BY id LIMIT ? OFFSET ?")
        queryParams.add(limit.toInt())
        queryParams.add(skip.toInt())

        val statement = connection.prepareStatement(searchQuery.toString())
        val countStatement = connection.prepareStatement(countQuery.toString())

        queryParams.forEachIndexed { index, param ->
            statement.setObject(index + 1, param)
            if (index < queryParams.size - 2) // dont add limit and skip to count query
                countStatement.setObject(index + 1, param)
        }

        val playersResult = statement.executeQuery()
        val countResult = countStatement.executeQuery()

        countResult.next()

        val total = countResult.getInt(1)
        val players = playersResult.getPlayers()

        Pair(players, total)

    } as Pair<List<Player>, Int>

    override fun update(id: UInt, value: Player): Boolean = execQuery { connection ->
        val statement = connection.prepareStatement(
            "UPDATE players SET name = ?, email = ? WHERE id = ?",
        )

        statement.setString(1, value.name.toString())
        statement.setString(2, value.email.toString())
        statement.setInt(3, id.toInt())
        val updated = statement.executeUpdate()

        updated > 0
    } as Boolean

    override fun delete(id: UInt): Boolean = execQuery { connection ->

        val statement = connection.prepareStatement(
            "DELETE FROM players WHERE id = ?",
        )

        statement.setInt(1, id.toInt())
        val deleted = statement.executeUpdate()

        deleted > 0
    } as Boolean

    override fun getByToken(token: UUID): Player? = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * FROM players WHERE token_hash = ?",
        )

        statement.setLong(1, token.hash())
        val resultSet = statement.executeQuery()

        resultSet.getPlayers().firstOrNull().also { statement.close() }
    } as Player?

    private fun UUID.hash(): Long {
        return leastSignificantBits xor mostSignificantBits
    }

    private fun ResultSet.getPlayers(): List<Player> {
        val players = mutableListOf<Player>()
        while (this.next()) {
            players += Player(
                this.getInt("id").toUInt(),
                Name(this.getString("name")),
                Email(this.getString("email")),
                this.getString("token_hash").toLong(),
            )
        }
        return players
    }

}
