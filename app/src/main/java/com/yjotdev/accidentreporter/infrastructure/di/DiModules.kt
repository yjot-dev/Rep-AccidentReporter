package com.yjotdev.accidentreporter.infrastructure.di

import dagger.Module
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.port.TokenPort
import com.yjotdev.accidentreporter.infrastructure.repository.ReportRepository
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
}