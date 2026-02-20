package com.yjotdev.accidentreporter.domain.entity

import com.google.gson.annotations.SerializedName

/**
 * Modelo que contiene las coordenadas geográficas (latitud y longitud).
 * Este es el modelo que directamente contiene los datos que se necesitan.
 *
 * @property lat La latitud de la ubicación.
 * @property lng La longitud de la ubicación.
 */
data class GeocodingEntity(
    @SerializedName("latitude")
    val lat: Double = 0.0,

    @SerializedName("longitude")
    val lng: Double = 0.0
)