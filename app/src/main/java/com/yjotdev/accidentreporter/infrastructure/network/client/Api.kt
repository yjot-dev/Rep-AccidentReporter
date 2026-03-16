package com.yjotdev.accidentreporter.infrastructure.network.client

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.BuildConfig
import com.yjotdev.accidentreporter.infrastructure.network.api.GeocodingApi
import com.yjotdev.accidentreporter.infrastructure.network.api.ReportApi
import com.yjotdev.accidentreporter.infrastructure.network.core.NullOnEmptyConverterFactory

@Singleton
class Api @Inject constructor(
    @ApplicationContext context: Context
) {
    private val url = if (BuildConfig.DEBUG) { "https://192.168.1.20:3000/api/" }
                      else { "https://servicio-api-ar-557625336820.us-central1.run.app/api/" }
    private val httpsClient = if (BuildConfig.DEBUG) { Client.getUnsafeClient(context) }
                              else { Client.getSafeClient() }

    /** API Tabla Reportes **/
    fun getReportsRetrofit(): ReportApi = Retrofit.Builder()
        .baseUrl(url)
        .client(httpsClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(NullOnEmptyConverterFactory())
        .build()
        .create(ReportApi::class.java)

    /** API Geocoding **/
    fun getGeocodingRetrofit(): GeocodingApi = Retrofit.Builder()
        .baseUrl(url)
        .client(httpsClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(NullOnEmptyConverterFactory())
        .build()
        .create(GeocodingApi::class.java)
}