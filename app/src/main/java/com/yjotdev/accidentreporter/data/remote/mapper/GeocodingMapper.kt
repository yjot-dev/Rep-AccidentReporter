package com.yjotdev.accidentreporter.data.remote.mapper

import com.yjotdev.accidentreporter.data.remote.dto.GeocodingDto
import com.yjotdev.accidentreporter.domain.model.GeocodingModel

/**
 * Mapea el objeto de red (DTO) al modelo de negocio (Domain).
 */
fun GeocodingDto.toDomain() = GeocodingModel(
    lat = this.lat,
    lng = this.lng
)

/**
 * Mapea el modelo de negocio (Domain) al objeto de red (DTO) para enviar a la API.
 */
fun GeocodingModel.toDto() = GeocodingDto(
    lat = this.lat,
    lng = this.lng
)