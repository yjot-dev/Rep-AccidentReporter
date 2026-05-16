package com.yjotdev.accidentreporter.data.repository

import android.content.Context
import androidx.core.content.edit
import javax.inject.Singleton
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository
import com.yjotdev.accidentreporter.domain.model.GeocodingModel
import com.yjotdev.accidentreporter.domain.core.mapSuccess
import com.yjotdev.accidentreporter.data.remote.core.safeApiCallForBody
import com.yjotdev.accidentreporter.data.remote.api.GeocodingApi
import com.yjotdev.accidentreporter.data.remote.mapper.toDomain

@Singleton
class GeocodingRepositoryImpl @Inject constructor(
    private val geocodingApi: GeocodingApi,
    @ApplicationContext context: Context
) : GeocodingRepository {
    private val sharedPreferences = context.getSharedPreferences("save_location", Context.MODE_PRIVATE)

    override suspend fun selectGeocoding(country: String, province: String, city: String): Result<GeocodingModel> {
        return safeApiCallForBody { geocodingApi.getCoordinates(country, province, city) }
            .mapSuccess { result -> result.toDomain() }
    }

    override fun getLocation(): String {
        return sharedPreferences.getString("location", "") ?: ""
    }

    override fun editLocation(location: String) {
        sharedPreferences.edit { putString("location", location) }
    }
}