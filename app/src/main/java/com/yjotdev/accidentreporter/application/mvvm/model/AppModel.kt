package com.yjotdev.accidentreporter.application.mvvm.model

import com.google.android.gms.maps.model.LatLng
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

/** Modelo de datos para los estados del viewmodel **/
data class AppModel(
    val textDescription: String = "",
    val indexComboBox: Int = 0,
    val itemsComboBox: List<String> = emptyList(),
    val showPosition: Boolean = false,
    val posMarker: LatLng = LatLng(0.0, 0.0),
    val indexMarker: Int = 0,
    val itemsMarker: List<ReportEntity> = emptyList(),
    val token: Int = 0,
    val enableUpdate: Boolean = false
)