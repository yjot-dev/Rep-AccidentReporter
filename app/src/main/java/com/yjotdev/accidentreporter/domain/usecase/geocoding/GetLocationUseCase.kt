package com.yjotdev.accidentreporter.domain.usecase.geocoding

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.GeocodingPort

@Singleton
class GetLocationUseCase @Inject constructor(
    private val geocodingPort: GeocodingPort
){
    operator fun invoke(): String {
        return geocodingPort.getLocation()
    }
}