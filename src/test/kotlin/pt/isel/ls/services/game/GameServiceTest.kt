package pt.isel.ls.services.game

import org.junit.jupiter.api.Test
import pt.isel.ls.storage.mem.SessionsDataMemGame

class GameServiceTest {

//    @Test
//    fun testGetGameById() {
//        val gameService = gameService()
//        val game = gameService.getGameById(1)
//        assertEquals(1, game.id)
//        assertEquals("The Legend of Zelda: Breath of the Wild", game.name)
//        assertEquals("Nintendo", game.publisher)
//        assertEquals("2017-03-03", game.releaseDate)
//        assertEquals("The Legend of Zelda: Breath of the Wild is an action-adventure game developed and published by Nintendo, released for the Nintendo Switch and Wii U consoles on March 3, 2017.", game.description)
//    }

    @Test
    fun testCreateGame_Success() {
        TODO()
    }

    @Test
    fun testCreateGame_GameNameAlreadyExists() {
        TODO()
    }

    @Test
    fun testGetGameDetails_Success() {
        TODO()
    }

    @Test
    fun testGetGameDetails_GameNotFound() {
        TODO()
    }

    @Test
    fun testSearchGames_Success() {
        TODO()
    }

    @Test
    fun testSearchGames_GenresNotFound() {
        TODO()
    }

    @Test
    fun testSearchGames_DeveloperNotFound() {
        TODO()
    }

    companion object {
        val mockGame = SessionsDataMemGame()
    }
}