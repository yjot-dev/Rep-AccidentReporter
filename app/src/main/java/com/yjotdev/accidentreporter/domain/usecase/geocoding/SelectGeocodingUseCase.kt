package com.yjotdev.accidentreporter.domain.usecase.geocoding

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.GeocodingEntity
import com.yjotdev.accidentreporter.domain.port.GeocodingPort

@Singleton
class SelectGeocodingUseCase @Inject constructor(
    private val geocodingPort: GeocodingPort
) {
    suspend operator fun invoke(country: String, province: String, city: String): Result<GeocodingEntity> {
        return geocodingPort.selectGeocoding(country, province, city)
    }
}