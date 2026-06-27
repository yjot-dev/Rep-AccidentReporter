package com.yjotdev.accidentreporter.utils.repositories

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.model.GeocodingModel
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository

@Singleton
class FakeGeocodingRepositoryImpl @Inject constructor(): GeocodingRepository {
    private val locationStorage = mutableMapOf<String, String>()

    override suspend fun selectGeocoding(
        country: String,
        province: String,
        city: String
    ): Result<GeocodingModel> {
        val location = GeocodingModel(lat = -3.245274, lng = -79.832028)
        return if(country.isNotEmpty() && province.isNotEmpty() && city.isNotEmpty()) {
            Result.Success(location)
        } else {
            Result.Error(Exception("Error al obtener geocoding"))
        }
    }

    override fun getLocation(): String {
        return locationStorage["location"] ?: ""
    }

    override fun editLocation(location: String) {
        locationStorage["location"] = location
    }
}