package com.yjotdev.accidentreporter.domain.port

import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.GeocodingEntity

/**
 * Define el contrato para la gestión de Geocoding y el guardado de la ubicación.
 * La ubicacion abarca el pais, la provincia y la ciudad.
 * Esta interfaz pertenece a la capa de Dominio y actúa como un puerto en
 * la Arquitectura Hexagonal.
 **/
interface GeocodingPort {
    suspend fun selectGeocoding(country: String, province: String, city: String): Result<GeocodingEntity>

    fun getLocation(): String

    fun editLocation(location: String)
}