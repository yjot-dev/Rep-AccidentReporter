package com.yjotdev.accidentreporter.domain.entity

import com.google.gson.annotations.SerializedName

/** Modelo de datos para el reporte de la BD **/
data class ReportEntity(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("latitude") val latitude: Double = 0.0,
    @SerializedName("longitude") val longitude: Double = 0.0,
    @SerializedName("date") val date: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("token") val token: Int = 0
)