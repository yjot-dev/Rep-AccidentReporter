package com.yjotdev.accidentreporter.domain.repository

import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.model.GeocodingModel

/**
 * Define el contrato para la gestión de Geocoding y el guardado de la ubicación.
 * La ubicacion abarca el pais, la provincia y la ciudad.
 * Esta interfaz pertenece a la capa de Dominio y actúa como un puerto en
 * la Arquitectura Hexagonal.
 **/
interface GeocodingRepository {
    suspend fun selectGeocoding(country: String, province: String, city: String): Result<GeocodingModel>

    fun getLocation(): String

    fun editLocation(location: String)
}