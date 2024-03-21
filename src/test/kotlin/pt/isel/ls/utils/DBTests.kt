package pt.isel.ls.utils


import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.postgresql.ds.PGSimpleDataSource
import kotlin.test.Test

class DBTests {


    @Test
    fun `test insert`(){
        dataSource.connection.use {
            val stm = it.prepareStatement("insert into courses(name) values ('LEIC');" +
                    "insert into students(course, number, name) values (1, 12345, 'Alice');" +
                    "insert into students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from courses where name = 'LEIC'")
            stm.executeUpdate()
        }
    }

    @Test
    fun `test students select`(){

        dataSource.connection.use {
            val stm = it.prepareStatement("select * from students")
            stm.executeQuery()
        }

    }

    @Test
    fun `test delete`(){
            dataSource.connection.use {
                val stm1 = it.prepareStatement("delete from courses cascade;" + "delete from students cascade")
                stm1.executeUpdate()
            }
    }

    @Test
    fun `test students update`() {
        dataSource.connection.use {
            val stm1 = it.prepareStatement("update students set name = 'Afonso' where name = 'Alice';" + "update courses set name = 'MEIC' where name = 'LEIC';")
            stm1.executeUpdate()
        }
    }

    companion object {
        val dataSource = PGSimpleDataSource()

        @JvmStatic
        @AfterAll
        fun `delete test tables`(): Unit {
            dataSource.connection.use {
                val stm = it.prepareStatement("drop table if exists students;" + "drop table if exists courses;")
                stm.executeUpdate()
            }
        }

        @JvmStatic
        @BeforeAll
        fun `connect and create test table`(): Unit {
            val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
            dataSource.setURL(jdbcDatabaseURL)
            dataSource.connection.use {
                val stm = it.prepareStatement(
                    "" +
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
    }


}