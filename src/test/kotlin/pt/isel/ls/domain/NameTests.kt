package pt.isel.ls.domain

import pt.isel.ls.data.domain.util.Name
import kotlin.test.Test
import kotlin.test.assertFailsWith

class NameTests {

    @Test
    fun `Name creation should succeed`() {
        val name = Name("Test Name")
    }

    @Test
    fun `Name creation should fail when name is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Name("")
        }
    }

    @Test
    fun `Name creation should fail when name is blank`() {
        assertFailsWith<IllegalArgumentException> {
            Name(" ")
        }
    }

    @Test
    fun `Name creation should fail when name is too small`() {
        assertFailsWith<IllegalArgumentException> {
            Name("12")
        }
    }

    @Test
    fun `Name creation should fail when name is too large`() {
        assertFailsWith<IllegalArgumentException> {
            Name("".plus("a".repeat(256)))
        }
    }
}
