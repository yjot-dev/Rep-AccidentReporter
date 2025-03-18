package com.yjotdev.accidentreporter.domain.entity

import com.google.gson.annotations.SerializedName

/** Modelo de datos para el reporte de la BD **/
data class ReportEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("date") val date: String,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("token") val token: Int
)