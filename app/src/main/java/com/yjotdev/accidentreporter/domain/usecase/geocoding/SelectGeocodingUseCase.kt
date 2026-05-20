package com.yjotdev.accidentreporter.domain.usecase.geocoding

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.model.GeocodingModel
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository

class SelectGeocodingUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository
) {
    suspend operator fun invoke(country: String, province: String, city: String): Result<GeocodingModel> {
        return geocodingRepository.selectGeocoding(country, province, city)
    }
}