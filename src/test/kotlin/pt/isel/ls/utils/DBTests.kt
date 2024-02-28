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

    @Test
    fun `test courses delete`(){
        try {
            // Delete all from the database
            dataSource.connection.use {
                // Courses
                val stm1 = it.prepareStatement("delete from courses cascade")
                stm1.executeUpdate()
                // Students
                val stm2 = it.prepareStatement("delete from students cascade")
                stm2.executeUpdate()
            }
        } catch (e: Exception) {
            // Print the exception
            e.printStackTrace()
        }
    }



}