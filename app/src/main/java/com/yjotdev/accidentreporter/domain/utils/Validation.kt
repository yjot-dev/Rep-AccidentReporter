package com.yjotdev.accidentreporter.domain.utils

import android.os.Build
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

object Validation {
    /** Convierte un marcador a posicion **/
    fun convertToPosition(marker: ReportEntity): LatLng {
        return LatLng(marker.latitude, marker.longitude)
    }
    /** Obtiene la fecha actual en String **/
    fun getDateToString(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            currentDateTime.format(formatter)
        } else { "" }
    }
}