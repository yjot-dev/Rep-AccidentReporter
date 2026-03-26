package com.yjotdev.accidentreporter.application.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

object Helper {
    /** Valida si un string es un numero **/
    fun isValidNumber(input: String): Boolean{
        return Regex("^[0-9]+\$").matches(input)
    }

    /** Valida si un string es un texto **/
    fun isValidText(input: String): Boolean{
        return Regex("^[A-Za-z ]+\$").matches(input)
    }

    /** Convierte un marcador a posicion **/
    fun convertToPosition(marker: ReportEntity): LatLng {
        return LatLng(marker.latitude, marker.longitude)
    }
    /** Obtiene la fecha actual en String **/
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateToString(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        return currentDateTime.format(formatter)
    }
}