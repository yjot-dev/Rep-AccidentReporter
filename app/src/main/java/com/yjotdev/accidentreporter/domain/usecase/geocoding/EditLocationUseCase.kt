package com.yjotdev.accidentreporter.domain.usecase.geocoding

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository

@Singleton
class EditLocationUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository
){
    operator fun invoke(location: String) {
        return geocodingRepository.editLocation(location)
    }
}