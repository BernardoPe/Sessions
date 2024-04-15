package pt.isel.ls.utils

import kotlin.test.Test
import kotlin.time.Duration.Companion.days

class TimeTests {

    @Test
    fun `test string to local date time`() {
        val date = "2021-05-01T00:00"
        val localDateTime = date.toLocalDateTime()
        assert(localDateTime.toString() == "2021-05-01T00:00")
    }

    @Test
    fun `test time after`() {
        val date1 = "2021-05-01T00:00"
        val date2 = "2021-05-01T00:01"
        val localDateTime1 = date1.toLocalDateTime()
        val localDateTime2 = date2.toLocalDateTime()
        assert(localDateTime2.isAfter(localDateTime1))
    }

    @Test
    fun `test time not after`() {
        val date1 = "2021-05-01T00:00"
        val date2 = "2021-05-01T00:01"
        val localDateTime1 = date1.toLocalDateTime()
        val localDateTime2 = date2.toLocalDateTime()
        assert(!localDateTime1.isAfter(localDateTime2))
    }

    @Test
    fun `test time before`() {
        val date1 = "2021-05-01T00:00"
        val date2 = "2021-05-01T00:01"
        val localDateTime1 = date1.toLocalDateTime()
        val localDateTime2 = date2.toLocalDateTime()
        assert(localDateTime1.isBefore(localDateTime2))
    }

    @Test
    fun `test time not before`() {
        val date1 = "2021-05-01T00:00"
        val date2 = "2021-05-01T00:01"
        val localDateTime1 = date1.toLocalDateTime()
        val localDateTime2 = date2.toLocalDateTime()
        assert(!localDateTime2.isBefore(localDateTime1))
    }

    @Test
    fun `test time to timestamp`() {
        val date = "2021-05-01T00:00"
        val localDateTime = date.toLocalDateTime()
        val timestamp = localDateTime.toTimestamp()
        assert(timestamp.toString() == "2021-05-01 00:00:00.0")
    }

    @Test
    fun `test to local date invalid format`() {
        val date = "2021-05-01"
        try {
            date.toLocalDateTime()
            assert(false)
        } catch (e: IllegalArgumentException) {
            assert(true)
        }
    }

    @Test
    fun `test time plus`() {
        val date = "2021-05-01T00:00"
        val localDateTime = date.toLocalDateTime()
        val newLocalDateTime = localDateTime + 1.days
        assert(newLocalDateTime.toString() == "2021-05-02T00:00")
    }

    //@Test
    //fun `test current local time`() {
    //    val currentLocalTime = currentLocalTime()
    //    val currentTimeLength = currentLocalTime.toString().length
    //     assert(currentTimeLength == 29)
    // }

}
