package pt.isel.ls.utils

import kotlin.test.Test

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
    fun `test time before`() {
        val date1 = "2021-05-01T00:00"
        val date2 = "2021-05-01T00:01"
        val localDateTime1 = date1.toLocalDateTime()
        val localDateTime2 = date2.toLocalDateTime()
        assert(localDateTime1.isBefore(localDateTime2))
    }
}
