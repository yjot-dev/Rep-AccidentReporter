package com.yjotdev.accidentreporter.application.mvvm.model

import com.google.android.gms.maps.model.LatLng
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

/** Modelo de datos para los estados del viewmodel **/
data class AppModel(
    //Estados del reporte
    val textDescription: String = "",
    val textToken: String = "",
    val indexComboBox: Int = 0,
    val itemsComboBox: List<String> = emptyList(),
    val showPosition: Boolean = false,
    val posMarker: LatLng = LatLng(0.0, 0.0),
    val indexMarker: Int = 0,
    val itemsMarker: List<ReportEntity>? = null,
    //Estados operativos
    val isLoading: Boolean = false,
    val enableUpdate: Boolean = false
)