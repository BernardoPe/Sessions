package pt.isel.ls.utils

import kotlin.test.Test

class TimeTests {

    @Test
    fun `test valid timestamp`() {
        // Arrange
        val timestamp = "2021-05-05 12:00:00"
        // Act
        val result = timestamp.isValidTimeStamp()
        // Assert
        assert(result)
    }

    @Test
    fun `test valid timestamp 2`() {
        // Arrange
        val timestamp = "2021-05-05 12:00:00.000"
        // Act
        val result = timestamp.isValidTimeStamp()
        // Assert
        assert(result)
    }

    @Test
    fun `test invalid timestamp`() {
        // Arrange
        val timestamp = "2021-05-23 12:00:00:000"
        // Act
        val result = timestamp.isValidTimeStamp()
        // Assert
        assert(!result)
    }

    @Test
    fun `test invalid timestamp2`() {
        // Arrange
        val timestamp = "2021-05- 12:0340:00.000"
        // Act
        val result = timestamp.isValidTimeStamp()
        // Assert
        assert(!result)
    }

    @Test
    fun `test valid timestamp to timestamp`() {
        // Arrange
        val timestamp = "2021-05-05 12:00:00"
        // Act
        val result = timestamp.toTimeStamp()
        // Assert
        assert(result.toString() == "2021-05-05 12:00:00.0")
    }

    @Test
    fun `test timestamp greater than`() {
        // Arrange
        val timestamp = "2021-05-05 12:00:00"
        val timestamp2 = "2021-05-05 12:00:01"
        // Act
        val result = timestamp.toTimeStamp().isGreaterThan(timestamp2.toTimeStamp())
        // Assert
        assert(!result)
    }
}