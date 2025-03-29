package com.yjotdev.accidentreporter.infrastructure.adapter

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.BuildConfig
import com.yjotdev.accidentreporter.domain.port.ReportRepository

@Singleton
class Api @Inject constructor(
    @ApplicationContext context: Context
) {
    private val httpsClient = if (BuildConfig.DEBUG) { Client.getUnsafeClient(context) }
                             else { Client.getSafeClient() }

    /** API **/
    fun getRetrofit(): ReportRepository = Retrofit.Builder()
        .baseUrl("https://192.168.1.20:443/api/")
        .client(httpsClient)
        .addConverterFactory(NullOnEmptyConverterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ReportRepository::class.java)
}