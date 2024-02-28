package pt.isel.ls.utils

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.postgresql.ds.PGSimpleDataSource

class DBTests {

    val dataSource = PGSimpleDataSource()
    @Before
    fun `connect and create test table`() {
        val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
        dataSource.setURL(jdbcDatabaseURL)
        dataSource.getConnection().use {
            val stm = it.prepareStatement("" +
                    "create table courses (\n" +
                    "  cid serial primary key,\n" +
                    "  name varchar(80)\n" +
                    ");\n" +
                    "\n" +
                    "create table students (\n" +
                    "  number int primary key,\n" +
                    "  name varchar(80),\n" +
                    "  course int references courses(cid)\n" +
                    ");"
            )
            stm.executeUpdate()
        }
    }
    @After
    fun `delete test tables`() {
        dataSource.getConnection().use {
            val stm = it.prepareStatement("drop table if exists students;" + "drop table if exists courses;")
            stm.executeUpdate()
        }
    }
    @Test
    fun `test insert`(){
        dataSource.getConnection().use {
            var stm = it.prepareStatement("insert into courses(name) values ('LEIC');")
            stm.executeUpdate()
            stm = it.prepareStatement("insert into students(course, number, name) values (1, 12345, 'Alice');")
            stm.executeUpdate()
            stm = it.prepareStatement("insert into students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from courses where name = 'LEIC'")
            stm.executeUpdate()
        }
    }

    @Test
    fun `test students select`(){

        dataSource.getConnection().use {
            val stm = it.prepareStatement("select * from students")
            stm.executeQuery()
        }

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