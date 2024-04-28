package pt.isel.ls.domain

import pt.isel.ls.data.domain.primitives.Genre
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GenreTests {

    @Test
    fun `Genre creation should succeed`() {
        Genre("RPG")
    }

    @Test
    fun `Genre creation should fail when genre is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Genre("")
        }
    }

    @Test
    fun `Genre creation should fail when genre is blank`() {
        assertFailsWith<IllegalArgumentException> {
            Genre(" ")
        }
    }

    @Test
    fun `Genre creation should fail when genre is too small`() {
        assertFailsWith<IllegalArgumentException> {
            Genre("12")
        }
    }

    @Test
    fun `Genre creation should fail when genre is too large`() {
        assertFailsWith<IllegalArgumentException> {
            Genre("".plus("a".repeat(256)))
        }
    }

}
