package pt.isel.ls.services.player

import org.junit.jupiter.api.BeforeEach
import pt.isel.ls.data.domain.player.Player
import pt.isel.ls.data.domain.primitives.Password
import pt.isel.ls.data.domain.primitives.PasswordHash
import pt.isel.ls.data.mapper.toEmail
import pt.isel.ls.data.mapper.toName
import pt.isel.ls.exceptions.BadRequestException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.services.PlayerService
import pt.isel.ls.storage.MemManager
import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class PlayerServiceTest {
    @Test
    fun testCreatePlayer_Success() {
        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = Player(2u, playerName, playerEmail, PasswordHash("TestPassword123#"))
        val createdPlayer = servicePlayer.createPlayer(player.name, player.email, Password("TestPassword123#"))

        // Checks if the created player ID is not null
        assertNotNull(createdPlayer.first)

        // Checks if the created player token is not null
        assertNotNull(createdPlayer.second)

        val getPlayer = servicePlayer.getPlayerDetails(createdPlayer.first)

        assertEquals(player.id, getPlayer.id)
        assertEquals(player.name, getPlayer.name)
        assertEquals(player.email, getPlayer.email)
    }

    @Test
    fun testCreatePlayer_EmailAlreadyExists() {
        val playerName1 = newTestPlayerName().toName()
        val playerEmail1 = newTestEmail().toEmail()

        val playerName2 = newTestPlayerName().toName()

        servicePlayer.createPlayer(playerName1, playerEmail1, Password("TestPassword123#"))

        val exception = assertFailsWith<BadRequestException> {
            servicePlayer.createPlayer(playerName2, playerEmail1, Password("TestPassword123#"))
        }
        assertEquals(400, exception.status)
        assertEquals("Bad Request", exception.description)
        assertEquals("Given Player email already exists", exception.errorCause)
    }

    @Test
    fun testGetPlayerDetails_Success() {
        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        val player = Player(2u, playerName, playerEmail, PasswordHash("TestPassword123#"))

        val createdPlayer = servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword123#"))
        val getPlayer = servicePlayer.getPlayerDetails(createdPlayer.first)

        assertEquals(player.id, getPlayer.id)
        assertEquals(player.name, getPlayer.name)
        assertEquals(player.email, getPlayer.email)
    }

    @Test
    fun testGetPlayerDetails_PlayerNotFound() {
        val randomId = Random.nextUInt()

        val exception = assertFailsWith<NotFoundException> {
            servicePlayer.getPlayerDetails(randomId)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Player not found", exception.errorCause)
    }

    @Test
    fun searchPlayersByNameTest() {
        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword123#"))

        val searchResult = servicePlayer.getPlayerList("pla".toName(), 10u, 0u)

        assertEquals(1, searchResult.first.size)
        assertEquals(1, searchResult.second)
        assertEquals(2u, searchResult.first[0].id)
        assertEquals(playerName, searchResult.first[0].name)
        assertEquals(playerEmail, searchResult.first[0].email)
    }

    @Test
    fun searchPlayersByNameTestNotFound() {
        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword123#"))

        val searchResult = servicePlayer.getPlayerList("yer".toName(), 10u, 0u)

        assertEquals(0, searchResult.first.size)
        assertEquals(0, searchResult.second)
    }


    @Test
    fun testCreatePlayerNameStored() {
        val playerName = newTestPlayerName().toName()
        val playerEmail = newTestEmail().toEmail()

        servicePlayer.createPlayer(playerName, playerEmail, Password("TestPassword123#"))

        val exception = assertFailsWith<BadRequestException> {
            servicePlayer.createPlayer(playerName, newTestEmail().toEmail(), Password("TestPassword123#"))
        }

        assertEquals(400, exception.status)
        assertEquals("Bad Request", exception.description)
        assertEquals("Given Player name already exists", exception.errorCause)
    }

    @BeforeEach
    fun clearStorage() {
        storage.close()
        servicePlayer = PlayerService(storage)
    }

    companion object {
        private fun newTestPlayerName() = "player-${abs(Random.nextLong())}"

        private fun newTestEmail() = "email-${abs(Random.nextLong())}@test.com"

        private var storage = MemManager()

        private var servicePlayer = PlayerService(storage)

    }
}
