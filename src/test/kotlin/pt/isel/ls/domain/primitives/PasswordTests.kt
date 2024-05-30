package pt.isel.ls.domain.primitives

import pt.isel.ls.data.domain.primitives.Password
import kotlin.test.Test
import kotlin.test.assertFailsWith

class PasswordTests {
    @Test
    fun `Password creation should succeed`() {
        Password("TestPassword123#")
    }

    @Test
    fun `Password creation should fail when password is empty`() {
        assertFailsWith<IllegalArgumentException> {
            Password("")
        }
    }

    @Test
    fun `Password creation should fail when password is blank`() {
        assertFailsWith<IllegalArgumentException> {
            Password(" ")
        }
    }

    @Test
    fun `Password creation should fail when password is too small`() {
        assertFailsWith<IllegalArgumentException> {
            Password("Test1#3")
        }
    }

    @Test
    fun `Password creation should fail when password is too large`() {
        assertFailsWith<IllegalArgumentException> {
            Password("".plus("a".repeat(256)))
        }
    }

    @Test
    fun `Password creation should fail when password does not contain a digit`() {
        assertFailsWith<IllegalArgumentException> {
            Password("TestPassword#")
        }
    }

    @Test
    fun `Password creation should fail when password does not contain a special character`() {
        assertFailsWith<IllegalArgumentException> {
            Password("TestPassword123")
        }
    }

    @Test
    fun `Password creation should fail when password does not contain an uppercase letter`() {
        assertFailsWith<IllegalArgumentException> {
            Password("testpassword123#")
        }
    }

    @Test
    fun `Password creation should fail when password does not contain a lowercase letter`() {
        assertFailsWith<IllegalArgumentException> {
            Password("TESTPASSWORD123#")
        }
    }

    @Test
    fun `Password creation should fail when password contains whitespace`() {
        assertFailsWith<IllegalArgumentException> {
            Password("Test Password123#")
        }
    }

}