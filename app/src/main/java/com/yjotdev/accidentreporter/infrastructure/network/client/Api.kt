package com.yjotdev.accidentreporter.infrastructure.network.client

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.BuildConfig
import com.yjotdev.accidentreporter.infrastructure.network.api.ReportApi
import com.yjotdev.accidentreporter.infrastructure.network.core.NullOnEmptyConverterFactory

@Singleton
class Api @Inject constructor(
    @field:Inject @ApplicationContext context: Context
) {
    private val httpsClient = if (BuildConfig.DEBUG) { Client.getUnsafeClient(context) }
                             else { Client.getSafeClient() }

    /** API **/
    fun getRetrofit(): ReportApi = Retrofit.Builder()
        .baseUrl("https://192.168.1.20:443/api/")
        .client(httpsClient)
        .addConverterFactory(NullOnEmptyConverterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ReportApi::class.java)
}