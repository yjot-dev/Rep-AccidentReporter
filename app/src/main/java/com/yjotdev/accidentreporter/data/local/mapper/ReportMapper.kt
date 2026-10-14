package com.yjotdev.accidentreporter.data.local.mapper

import com.yjotdev.accidentreporter.data.local.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.model.ReportModel

fun ReportEntity.toDomain() = ReportModel(
    id = this.id,
    latitude = this.latitude,
    longitude = this.longitude,
    date = this.date,
    type = this.type,
    description = this.description,
    token = this.token
)

fun ReportModel.toBD() = ReportEntity(
    id = this.id,
    latitude = this.latitude,
    longitude = this.longitude,
    date = this.date,
    type = this.type,
    description = this.description,
    token = this.token
)