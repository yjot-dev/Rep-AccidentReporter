package com.yjotdev.accidentreporter.infrastructure.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.yjotdev.accidentreporter.domain.entity.GeocodingEntity

interface GeocodingApi {
    @GET("geocoding")
    suspend fun getCoordinates(
        @Query("pais") country: String,
        @Query("provincia") province: String,
        @Query("ciudad") city: String
    ): Response<GeocodingEntity>
}