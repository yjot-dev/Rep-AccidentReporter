package com.yjotdev.accidentreporter.data.remote.mapper

import com.yjotdev.accidentreporter.data.remote.dto.ReportDto
import com.yjotdev.accidentreporter.domain.model.ReportModel

/**
 * Mapea el objeto de red (DTO) al modelo de negocio (Domain).
 */
fun ReportDto.toDomain() = ReportModel(
    id = this.id,
    latitude = this.latitude,
    longitude = this.longitude,
    date = this.date,
    type = this.type,
    description = this.description,
    token = this.token
)

/**
 * Mapea el modelo de negocio (Domain) al objeto de red (DTO) para enviar a la API.
 */
fun ReportModel.toDto() = ReportDto(
    id = this.id,
    latitude = this.latitude,
    longitude = this.longitude,
    date = this.date,
    type = this.type,
    description = this.description,
    token = this.token
)