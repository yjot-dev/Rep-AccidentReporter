package com.yjotdev.accidentreporter.infrastructure.di

import dagger.Module
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.GeocodingPort
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.port.StringPort
import com.yjotdev.accidentreporter.domain.port.TokenPort
import com.yjotdev.accidentreporter.infrastructure.repository.GeocodingRepository
import com.yjotdev.accidentreporter.infrastructure.repository.ReportRepository
import com.yjotdev.accidentreporter.infrastructure.repository.StringRepository
import com.yjotdev.accidentreporter.infrastructure.repository.TokenRepository

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {
    @Binds
    @Singleton
    abstract fun bindReportRepository(
        impl: ReportRepository
    ): ReportPort

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        impl: TokenRepository
    ): TokenPort

    @Binds
    @Singleton
    abstract fun bindStringRepository(
        impl: StringRepository
    ): StringPort

    @Binds
    @Singleton
    abstract fun bindGeocodingRepository(
        impl: GeocodingRepository
    ): GeocodingPort
}