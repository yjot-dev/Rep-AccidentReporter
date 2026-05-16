package com.yjotdev.accidentreporter.data.di

import dagger.Module
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit
import com.yjotdev.accidentreporter.data.remote.api.GeocodingApi
import com.yjotdev.accidentreporter.data.remote.api.ReportApi
import com.yjotdev.accidentreporter.data.remote.network.RetrofitBuilder
import com.yjotdev.accidentreporter.data.repository.GeocodingRepositoryImpl
import com.yjotdev.accidentreporter.data.repository.ReportRepositoryImpl
import com.yjotdev.accidentreporter.data.repository.StringRepositoryImpl
import com.yjotdev.accidentreporter.data.repository.TokenRepositoryImpl
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository
import com.yjotdev.accidentreporter.domain.repository.ReportRepository
import com.yjotdev.accidentreporter.domain.repository.StringRepository
import com.yjotdev.accidentreporter.domain.repository.TokenRepository

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {

    // --- BINDINGS (Abstracciones) ---
    @Binds
    @Singleton
    abstract fun bindReportRepository(
        impl: ReportRepositoryImpl
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        impl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    abstract fun bindStringRepository(
        impl: StringRepositoryImpl
    ): StringRepository

    @Binds
    @Singleton
    abstract fun bindGeocodingRepository(
        impl: GeocodingRepositoryImpl
    ): GeocodingRepository

    // --- PROVIDERS (Instancias externas) ---
    companion object {
        @Provides
        @Singleton
        fun provideRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit {
            return retrofitBuilder.getRetrofitInstance()
        }

        @Provides
        @Singleton
        fun provideReportApi(retrofit: Retrofit): ReportApi {
            return retrofit.create(ReportApi::class.java)
        }

        @Provides
        @Singleton
        fun provideGeocodingApi(retrofit: Retrofit): GeocodingApi {
            return retrofit.create(GeocodingApi::class.java)
        }
    }
}