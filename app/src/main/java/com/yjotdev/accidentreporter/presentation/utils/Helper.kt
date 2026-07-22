package com.yjotdev.accidentreporter.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.yjotdev.accidentreporter.domain.model.ReportModel

object Helper {
    /** Valida si un string es un token Hexadecimal **/
    fun isValidToken(input: String): Boolean{
        return Regex("^[a-fA-F0-9]{32}$").matches(input)
    }

    /** Valida si un string es un mensaje **/
    fun isValidMessage(input: String): Boolean{
        return Regex("^[A-Za-z.,\\s]{1,300}$").matches(input)
    }

    /** Convierte un marcador a posicion **/
    fun convertToPosition(marker: ReportModel): LatLng {
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