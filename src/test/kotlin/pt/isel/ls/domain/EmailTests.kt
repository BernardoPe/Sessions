package pt.isel.ls.domain

import pt.isel.ls.data.domain.util.Email
import pt.isel.ls.data.mapper.toEmail
import kotlin.test.Test
import kotlin.test.assertFailsWith

class EmailTests {
    @Test
    fun `Email creation should succeed`() {
        val email = Email("testemail@test.pt")
        assert(email.email == "testemail@test.pt")
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

    @Test
    fun `Email creation should fail with a blank email`() {
        assertFailsWith<IllegalArgumentException> {
            Email(" ")
        }
    }

    @Test
    fun `Email toString should return the email`() {
        val email = Email("testemail@test.pt")
        assert(email.toString() == "testemail@test.pt")
    }

    @Test
    fun `String toEmail should return an Email`() {
        val email = "testemail@test.pt".toEmail()
        assert(email.email == "testemail@test.pt")
    }

}
