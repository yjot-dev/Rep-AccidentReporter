package com.yjotdev.accidentreporter.domain.usecase.geocoding

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository

class GetLocationUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository
){
    operator fun invoke(): String {
        return geocodingRepository.getLocation()
    }
}