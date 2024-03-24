package pt.isel.ls.storage.db

import java.sql.Connection
import pt.isel.ls.data.domain.Email
import pt.isel.ls.data.domain.Name
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.storage.SessionsDataPlayer
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class SessionsDataDBPlayer(private val connection : Connection) : SessionsDataPlayer {

    init {
        connection.autoCommit = false
    }

    override fun create(player : Player): Pair<UInt, UUID> {
        val statement = connection.prepareStatement(
            "INSERT INTO players (name, email,token_hash) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )

        val token = UUID.randomUUID()

        statement.setString(1, player.name.toString())
        statement.setString(2, player.email.toString())
        statement.setLong(3, token.hash())
        statement.executeUpdate()
        connection.commit()

        val generatedKeys = statement.generatedKeys
        generatedKeys.next()
        return Pair(generatedKeys.getInt(1).toUInt(),token)
    }

    override fun getById(id: UInt): Player? {
        val statement = connection.prepareStatement(
            "SELECT * FROM players WHERE id = ?"
        )

        statement.setInt(1, id.toInt())
        val resultSet = statement.executeQuery()
        connection.commit()

        return resultSet.getPlayers().firstOrNull().also { statement.close() }
    }

    override fun isEmailStored(email: Email): Boolean {
        val statement = connection.prepareStatement(
            "SELECT * FROM players WHERE email = ?"
        )

        statement.setString(1, email.toString())
        val resultSet = statement.executeQuery()
        connection.commit()

        return resultSet.next().also { statement.close() }
    }

    override fun getAll(): List<Player> {
        val statement = connection.prepareStatement(
            "SELECT * FROM players"
        )

        val resultSet = statement.executeQuery()
        connection.commit()

        return resultSet.getPlayers().also { statement.close() }
    }

    override fun update(id: UInt, value: Player): Boolean {
        val statement = connection.prepareStatement(
            "UPDATE players SET name = ?, email = ? WHERE id = ?"
        )

        statement.setString(1, value.name.toString())
        statement.setString(2, value.email.toString())
        statement.setInt(3, id.toInt())
        val updated = statement.executeUpdate()
        connection.commit()

        return updated > 0
    }

    override fun delete(id: UInt): Boolean {
        val statement = connection.prepareStatement(
            "DELETE FROM players WHERE id = ?"
        )

        statement.setInt(1, id.toInt())
        val deleted = statement.executeUpdate()
        connection.commit()

        return deleted > 0
    }

    override fun getByToken(token: UUID): Player? {
        val statement = connection.prepareStatement(
            "SELECT * FROM players WHERE token_hash = ?"
        )

        statement.setLong(1, token.hash())
        val resultSet = statement.executeQuery()
        connection.commit()

        return resultSet.getPlayers().firstOrNull().also { statement.close() }
    }

    private fun UUID.hash() : Long {
        return leastSignificantBits xor mostSignificantBits
    }


    private fun ResultSet.getPlayers(): List<Player> {

         var players = listOf<Player>()
         while (this.next()) {
            players += Player(
                this.getInt("id").toUInt(),
                Name(this.getString("name")),
                Email(this.getString("email")),
                this.getString("token_hash").toLong()
            )
        }
        return players
    }

}