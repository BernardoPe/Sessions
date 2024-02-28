package pt.isel.ls.utils

import org.junit.Before
import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource

import kotlin.test.assertTrue

class DBTests {

    val dataSource = PGSimpleDataSource()
    @Before
    fun connect() {
        val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
        dataSource.setURL(jdbcDatabaseURL)
    }

    @Test
    fun `test students select`(){

        val studentsList = mutableListOf<String>()

        dataSource.getConnection().use {
            val stm = it.prepareStatement("select * from students")
            val rs = stm.executeQuery()
            while (rs.next()) {
                studentsList.add(rs.getString("name"))
            }
        }
        assertTrue { studentsList.size == 2 && studentsList.contains("Alice") && studentsList.contains("Bob") }

    }



}