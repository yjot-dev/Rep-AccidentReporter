package com.yjotdev.accidentreporter.domain.model

/** Modelo de datos para el reporte de la BD **/
data class ReportModel(
    val id: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val date: String = "",
    val type: String = "",
    val description: String = "",
    val token: String = ""
)