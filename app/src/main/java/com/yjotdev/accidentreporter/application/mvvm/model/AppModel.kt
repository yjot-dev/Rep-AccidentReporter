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
    val itemsMarker: List<ReportEntity>? = null,
    val token: Int = 0,
    val enableUpdate: Boolean = false,
    val isLoading: Boolean = false,
    val wasFound: Boolean = false,
    val wasInserted: Boolean = false,
    val wasUpdated: Boolean = false,
    val wasDeleted: Boolean = false,
    val error: String? = null,
    val operationCompletedCount: Int = 0
)