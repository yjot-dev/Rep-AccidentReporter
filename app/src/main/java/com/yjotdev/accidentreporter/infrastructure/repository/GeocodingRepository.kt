package com.yjotdev.accidentreporter.infrastructure.repository

import android.content.Context
import androidx.core.content.edit
import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.GeocodingEntity
import com.yjotdev.accidentreporter.domain.port.GeocodingPort
import com.yjotdev.accidentreporter.infrastructure.network.client.Api
import com.yjotdev.accidentreporter.infrastructure.network.core.safeApiCallForBody
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class GeocodingRepository @Inject constructor(
    private val api: Api,
    @ApplicationContext context: Context
) : GeocodingPort {
    private val sharedPreferences = context.getSharedPreferences("save_location", Context.MODE_PRIVATE)

    override suspend fun selectGeocoding(country: String, province: String, city: String): Result<GeocodingEntity> {
        return safeApiCallForBody { api.getGeocodingRetrofit().getCoordinates(country, province, city) }
    }

    override fun getLocation(): String {
        return sharedPreferences.getString("location", "") ?: ""
    }

    override fun editLocation(location: String) {
        sharedPreferences.edit { putString("location", location) }
    }
}