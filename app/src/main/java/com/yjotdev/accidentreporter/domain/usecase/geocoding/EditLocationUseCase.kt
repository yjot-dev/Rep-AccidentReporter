package com.yjotdev.accidentreporter.domain.usecase.geocoding

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.GeocodingPort

@Singleton
class EditLocationUseCase @Inject constructor(
    private val geocodingPort: GeocodingPort
){
    operator fun invoke(location: String) {
        return geocodingPort.editLocation(location)
    }
}