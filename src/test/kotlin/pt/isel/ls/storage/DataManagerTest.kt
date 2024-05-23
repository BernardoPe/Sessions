package pt.isel.ls.storage

import pt.isel.ls.storage.db.SessionsDataDBGame
import pt.isel.ls.storage.db.SessionsDataDBPlayer
import pt.isel.ls.storage.db.SessionsDataDBSession
import pt.isel.ls.storage.mem.SessionsDataMemGame
import pt.isel.ls.storage.mem.SessionsDataMemPlayer
import pt.isel.ls.storage.mem.SessionsDataMemSession
import kotlin.test.Test
import kotlin.test.assertTrue

class DataManagerTest {

    @Test
    fun `test create memory data manager`() {
        // Arrange
        val dataManager = MemManager()
        assertTrue { dataManager.game is SessionsDataMemGame }
        assertTrue { dataManager.player is SessionsDataMemPlayer }
        assertTrue { dataManager.session is SessionsDataMemSession }
    }

    @Test
    fun `test create database memory manager`() {
        // Arrange
        val dataManager = DBManager("url")
        assertTrue { dataManager.game is SessionsDataDBGame }
        assertTrue { dataManager.player is SessionsDataDBPlayer }
        assertTrue { dataManager.session is SessionsDataDBSession }
    }

}