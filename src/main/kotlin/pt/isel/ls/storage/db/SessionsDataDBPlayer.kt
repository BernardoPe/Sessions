package pt.isel.ls.storage.db

import kotlinx.datetime.toKotlinLocalDateTime
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.player.Token
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.storage.SessionsDataPlayer
import pt.isel.ls.utils.toTimestamp
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class SessionsDataDBPlayer(dbURL: String) : SessionsDataPlayer, DBManager(dbURL) {

    @Suppress("UNCHECKED_CAST")
    override fun create(player: Player): Pair<UInt, Token> = execQuery { connection ->


        val statement = connection.prepareStatement(
            "INSERT INTO players (name, email, password_hash) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS,
        )

        statement.setString(1, player.name.toString())
        statement.setString(2, player.email.toString())
        statement.setString(3, player.password.toString())
        statement.executeUpdate()

        val generatedKeys = statement.generatedKeys
        generatedKeys.next()

        val playerId = generatedKeys.getInt(1).toUInt()

        Pair(playerId, tokenCreation(connection, generatedKeys.getInt(1).toUInt()))

    } as Pair<UInt, Token>

    @Suppress("UNCHECKED_CAST")
    override fun login(id: UInt): Pair<UInt, Token> = execQuery { connection ->
        Pair(id, tokenCreation(connection, id))
    } as Pair<UInt, Token>

    override fun getById(id: UInt): Player? = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * FROM players WHERE id = ?",
        )
        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()

        resultSet.getPlayers().firstOrNull().also { statement.close() }
    } as Player?

    @Suppress("UNCHECKED_CAST")
    override fun getAll(): List<Player> = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * FROM players",
        )

        val resultSet = statement.executeQuery()

        resultSet.getPlayers().also { statement.close() }
    } as List<Player>

    @Suppress("UNCHECKED_CAST")
    override fun getPlayersSearch(name: Name?, limit: UInt, skip: UInt): Pair<List<Player>, Int> =
        execQuery { connection ->

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

    // Redundant query
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

    // Redundant query
    override fun delete(id: UInt): Boolean = execQuery { connection ->

        val statement = connection.prepareStatement(
            "DELETE FROM players WHERE id = ?",
        )

        statement.setInt(1, id.toInt())
        val deleted = statement.executeUpdate()

        deleted > 0
    } as Boolean

    @Suppress("UNCHECKED_CAST")
    override fun getPlayerAndToken(token: UUID): Pair<Player, Token>? = execQuery { connection ->
        val statement = connection.prepareStatement(
            "SELECT * " +
                    "FROM players " +
                    "JOIN tokens ON players.id = tokens.player_id " +
                    "WHERE tokens.token = ?;",
        )

        statement.setString(1, token.toString())
        val resultSet = statement.executeQuery()

        if (resultSet.next()) {
            val playerObj = Player(
                resultSet.getInt("id").toUInt(),
                Name(resultSet.getString("name")),
                Email(resultSet.getString("email")),
                PasswordHash(resultSet.getString("password_hash"))
            )

            val tokenObj = Token(
                UUID.fromString(resultSet.getString("token")),
                resultSet.getInt("player_id").toUInt(),
                resultSet.getTimestamp("timeCreation").toLocalDateTime().toKotlinLocalDateTime(),
                resultSet.getTimestamp("timeExpiration").toLocalDateTime().toKotlinLocalDateTime()
            )
            Pair(playerObj, tokenObj).also { statement.close() }
        } else {
            statement.close()
            null
        }
    } as Pair<Player, Token>?

    override fun revokeToken(token: UUID): Boolean = execQuery { connection ->
        val statement = connection.prepareStatement(
            "DELETE FROM tokens WHERE token = ?",
        )

        statement.setString(1, token.toString())
        val updated = statement.executeUpdate()

        updated > 0
    } as Boolean

    private fun ResultSet.getPlayers(): List<Player> {
        val players = mutableListOf<Player>()
        while (this.next()) {
            players += Player(
                this.getInt("id").toUInt(),
                Name(this.getString("name")),
                Email(this.getString("email")),
                PasswordHash(this.getString("password_hash"))
            )
        }
        return players
    }

    private fun tokenCreation(connection: Connection, playerId: UInt): Token {
        val statement = connection.prepareStatement(
            "INSERT INTO tokens (token, player_id, timeCreation, timeExpiration) VALUES (?, ?, ?, ?)",
        )

        val token = Token(UUID.randomUUID(), playerId)

        statement.setString(1, token.token.toString())
        statement.setInt(2, token.playerId.toInt())
        statement.setTimestamp(3, token.timeCreation.toTimestamp())
        statement.setTimestamp(3, token.timeExpiration.toTimestamp())
        statement.executeUpdate()

        return token
    }

}
