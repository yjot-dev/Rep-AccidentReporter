package com.yjotdev.accidentreporter.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.yjotdev.accidentreporter.data.remote.dto.GeocodingDto

interface GeocodingApi {
    @GET("geocoding")
    suspend fun getCoordinates(
        @Query("pais") country: String,
        @Query("provincia") province: String,
        @Query("ciudad") city: String
    ): Response<GeocodingDto>
}