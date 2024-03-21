package pt.isel.ls.domain

import pt.isel.ls.data.domain.Email
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EmailTests {
    @Test
    fun `Email creation should succeed`() {
        val email = Email("testemail@test.pt")
    }

    @Test
    fun `Email creation should fail when email is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Email("")
        }
    }

    @Test
    fun `Email creation should fail when email is invalid`() {
        assertFailsWith<IllegalArgumentException> {
            Email("testemail")
        }
    }

    @Test
    fun `Email creation should fail when email is invalid 2`() {
        assertFailsWith<IllegalArgumentException> {
            Email("testmail@gmoal")
        }
    }


}