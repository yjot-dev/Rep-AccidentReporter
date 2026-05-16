package com.yjotdev.accidentreporter.presentation.mvvm.state

import com.google.android.gms.maps.model.LatLng
import com.yjotdev.accidentreporter.domain.model.ReportModel

/** Modelo de datos para los estados del viewmodel **/
data class UiState(
    //Estados de configuracion
    val textToken: String = "",
    val textCountry: String = "",
    val textProvince: String = "",
    val textCity: String = "",
    val location: LatLng = LatLng(0.0, 0.0),
    //Estados del reporte
    val textDescription: String = "",
    val indexComboBox: Int = 0,
    val itemsComboBox: List<String> = emptyList(),
    val showPosition: Boolean = false,
    val posMarker: LatLng = LatLng(0.0, 0.0),
    val indexMarker: Int = 0,
    val itemsMarker: List<ReportModel> = emptyList(),
    //Estados operativos
    val isLoading: Boolean = false,
    val enableUpdate: Boolean = false
)