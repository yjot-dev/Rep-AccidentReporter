package com.yjotdev.accidentreporter

import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDateTime
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.utils.Validation

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ValidationUnitTest {
    @Test
    fun convertToPositionTest() {
        val marker = ReportEntity(
            id = 0,
            latitude = -79.097823,
            longitude = -56.983451,
            date = "",
            type = "",
            description = "",
            token = 0
        )
        //Verifica que la posicion sea la misma
        val actual = Validation.convertToPosition(marker)
        val expected = LatLng(-79.097823, -56.983451)
        assertEquals(expected, actual)
    }

    @Test
    fun getDateToStringTest(){
        val date = LocalDateTime.now()
        val day = if(date.dayOfMonth < 10){ "0${date.dayOfMonth}" }else{ date.dayOfMonth }
        val month = if(date.monthValue < 10){ "0${date.monthValue}" }else{ date.monthValue }
        val hour = if(date.hour < 10){ "0${date.hour}" }else{ date.hour }
        val minute = if(date.minute < 10){ "0${date.minute}" }else{ date.minute }
        //Verifica que la fecha sea la misma
        val actual = Validation.getDateToString()
        val expected = "$day-$month-${date.year} $hour:$minute:"
        assertTrue(actual.contains(expected))
    }
}