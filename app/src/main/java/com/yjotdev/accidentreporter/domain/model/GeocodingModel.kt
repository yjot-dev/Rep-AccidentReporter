package com.yjotdev.accidentreporter.domain.model

/**
 * Modelo que contiene las coordenadas geográficas (latitud y longitud).
 * Este es el modelo que directamente contiene los datos que se necesitan.
 *
 * @property lat La latitud de la ubicación.
 * @property lng La longitud de la ubicación.
 */
data class GeocodingModel(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)