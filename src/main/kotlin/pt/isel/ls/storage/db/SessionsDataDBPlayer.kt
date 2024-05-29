package pt.isel.ls.storage.db

import kotlinx.datetime.toKotlinLocalDateTime
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.player.Token
import pt.isel.ls.data.domain.primitives.Email
import pt.isel.ls.data.domain.primitives.Name
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.storage.SessionsDataPlayer
import pt.isel.ls.utils.toTimestamp
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class SessionsDataDBPlayer(private val dbURL: String) : SessionsDataPlayer {

    private val connection get() = TransactionManager.getConnection(dbURL)
    override fun create(player: Player): Pair<UInt, Token> {

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

        return Pair(playerId, tokenCreation(generatedKeys.getInt(1).toUInt()))

    }

    override fun login(id: UInt): Pair<UInt, Token> {
        return Pair(id, tokenCreation(id))
    }

    override fun getById(id: UInt): Player? {
        val statement = connection.prepareStatement(
            "SELECT * FROM players WHERE id = ?",
        )
        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()

        return resultSet.getPlayers().firstOrNull().also { statement.close() }
    }

    override fun getAll(): List<Player> {
        val statement = connection.prepareStatement(
            "SELECT * FROM players",
        )

        val resultSet = statement.executeQuery()

        return resultSet.getPlayers().also { statement.close() }
    }

    override fun getPlayersSearch(name: Name?, limit: UInt, skip: UInt): Pair<List<Player>, Int> {

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

            return Pair(players, total)

        }

    override fun update(id: UInt, value: Player): Boolean {
        val statement = connection.prepareStatement(
            "UPDATE players SET name = ?, email = ? WHERE id = ?",
        )

        statement.setString(1, value.name.toString())
        statement.setString(2, value.email.toString())
        statement.setInt(3, id.toInt())
        val updated = statement.executeUpdate()

        return updated > 0
    }

    override fun delete(id: UInt): Boolean {

        val statement = connection.prepareStatement(
            "DELETE FROM players WHERE id = ?",
        )

        statement.setInt(1, id.toInt())
        val deleted = statement.executeUpdate()

        return deleted > 0
    }


    override fun getPlayerAndToken(token: UUID): Pair<Player, Token>? {

        val statement = connection.prepareStatement(
            "SELECT * " +
                    "FROM players " +
                    "JOIN tokens ON players.id = tokens.player_id " +
                    "WHERE tokens.token = ?;",
        )

        statement.setString(1, token.toString())
        val resultSet = statement.executeQuery()

        return if (resultSet.next()) {
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
            null
        }.also { statement.close() }
    }

    override fun isEmailStored(email: Email): Boolean {
        val statement = connection.prepareStatement(
            "SELECT 1 FROM players WHERE email = ?",
        )

        statement.setString(1, email.toString())
        val resultSet = statement.executeQuery()

        return resultSet.next().also { statement.close() }
    }

    override fun isNameStored(name: Name): Boolean {
        val statement = connection.prepareStatement(
            "SELECT 1 FROM players WHERE name = ?",
        )

        statement.setString(1, name.toString())
        val resultSet = statement.executeQuery()

        return resultSet.next().also { statement.close() }
    }

    override fun revokeToken(token: UUID): Boolean {
        val statement = connection.prepareStatement(
            "DELETE FROM tokens WHERE token = ?",
        )

        statement.setString(1, token.toString())
        val updated = statement.executeUpdate()

        return updated > 0
    }

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

    private fun tokenCreation(playerId: UInt): Token {

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
