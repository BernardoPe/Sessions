package pt.isel.ls.services.game

import org.junit.jupiter.api.Test
import pt.isel.ls.data.domain.Genre
import pt.isel.ls.data.domain.game.Game
import pt.isel.ls.data.domain.toName
import pt.isel.ls.exceptions.ConflictException
import pt.isel.ls.exceptions.NotFoundException
import pt.isel.ls.services.GameService
import pt.isel.ls.storage.SessionsDataManager
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class GameServiceTest {
    @Test
    fun testCreateGame_Success() {
        val gameName = newTestGameName1().toName()
        val developer = newTestDeveloper1().toName()
        val genres = newTestGenres()

        val createdGame = serviceGame.createGame(gameName, developer, genres)

        assertNotNull(createdGame)

        val getGame = serviceGame.getGameById(createdGame)

        assertEquals(Game(createdGame, gameName, developer, genres), getGame)
    }

    @Test
    fun testCreateGame_GameNameAlreadyExists() {
        val gameName = newTestGameName1().toName()
        val developer = newTestDeveloper1().toName()
        val genres = newTestGenres()

        serviceGame.createGame(gameName, developer, genres)

        val exception = assertFailsWith<ConflictException> {
            serviceGame.createGame(gameName, developer, genres)
        }

        assertEquals("Conflict", exception.description)
        assertEquals("Game name already exists", exception.errorCause)
    }

    @Test
    fun testGetGameDetails_Success() {
        val gameName = newTestGameName1().toName()
        val developer = newTestDeveloper1().toName()
        val genres = newTestGenres()

        val createdGame = serviceGame.createGame(gameName, developer, genres)

        val getGame = serviceGame.getGameById(createdGame)

        assertEquals(Game(createdGame, gameName, developer, genres), getGame)
    }

    @Test
    fun testGetGameDetails_GameNotFound() {
        val randomId = Random.nextUInt()

        val exception = assertFailsWith<NotFoundException> {
            serviceGame.getGameById(randomId)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Game not found", exception.errorCause)
    }

    @Test
    fun testSearchGames_Success() {
        val gameName1 = newTestGameName1().toName()
        val developer = newTestDeveloper1().toName()
        val genres = newTestGenres()
        val gameName2 = newTestGameName2().toName()
        val limit = 5u
        val skip = 0u

        val createdGame1 = serviceGame.createGame(gameName1, developer, genres)
        val createdGame2 = serviceGame.createGame(gameName2, developer, genres)

        assertNotNull(createdGame1)
        assertNotNull(createdGame2)

        val gameSearched = serviceGame.searchGames(genres, developer, limit, skip)

        assertEquals(
            listOf(Game(createdGame1, gameName1, developer, genres), Game(createdGame2, gameName2, developer, genres)),
            gameSearched
        )
    }

    @Test
    fun testSearchGames_DeveloperNotFound() {
        val gameName1 = newTestGameName1().toName()
        val developer1 = newTestDeveloper1().toName()
        val developer2 = newTestDeveloper2().toName()
        val genres = newTestGenres()
        val gameName2 = newTestGameName2().toName()
        val limit = 5u
        val skip = 0u

        val createdGame1 = serviceGame.createGame(gameName1, developer1, genres)
        val createdGame2 = serviceGame.createGame(gameName2, developer1, genres)

        assertNotNull(createdGame1)
        assertNotNull(createdGame2)

        val exception = assertFailsWith<NotFoundException> {
            serviceGame.searchGames(genres, developer2, limit, skip)
        }

        assertEquals("Not Found", exception.description)
        assertEquals("Developer not found", exception.errorCause)

    }

    companion object {

        private fun newTestGameName1() = "Skyrim"

        private fun newTestGameName2() = "Pokemon"

        private fun newTestDeveloper1() = "Bethesda"

        private fun newTestDeveloper2() = "Game Freak"

        private fun newTestGenres() = setOf(Genre("RPG"), Genre("Adventure"))

        private val storage =
            SessionsDataManager(SessionsDataMemGame(), SessionsDataMemPlayer(), SessionsDataMemSession())

        private val serviceGame = GameService(storage)
    }
}