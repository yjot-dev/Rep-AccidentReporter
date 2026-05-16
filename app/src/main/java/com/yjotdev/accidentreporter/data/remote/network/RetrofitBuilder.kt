package com.yjotdev.accidentreporter.data.remote.network

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import com.yjotdev.accidentreporter.BuildConfig
import com.yjotdev.accidentreporter.data.remote.core.NullOnEmptyConverterFactory

@Singleton
class RetrofitBuilder @Inject constructor() {
    // Configuración dinámica de URL según el entorno
    private val baseUrl = BuildConfig.API_BASE_URL

    // Configuración de log interceptor
    private val loggingInterceptor = HttpLoggingInterceptor { msm ->
        Log.d("Https", msm)
    }.apply { level = HttpLoggingInterceptor.Level.BODY }

    // Configuración de cliente HTTP
    private val client = okhttp3.OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    /**
     * Construye y devuelve una instancia configurada de Retrofit.
     * Este es ahora el único punto de configuración de red para toda la app.
     */
    fun getRetrofitInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(NullOnEmptyConverterFactory())
        .build()
}