package com.yjotdev.accidentreporter.utils.di

import dagger.Module
import dagger.Binds
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository
import com.yjotdev.accidentreporter.domain.repository.ReportDaoRepository
import com.yjotdev.accidentreporter.domain.repository.StringRepository
import com.yjotdev.accidentreporter.domain.repository.TokenRepository
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository
import com.yjotdev.accidentreporter.data.di.DiModules
import com.yjotdev.accidentreporter.utils.repositories.FakeGeocodingRepositoryImpl
import com.yjotdev.accidentreporter.utils.repositories.FakeReportApiRepositoryImpl
import com.yjotdev.accidentreporter.utils.repositories.FakeReportDaoRepositoryImpl
import com.yjotdev.accidentreporter.utils.repositories.FakeStringRepositoryImpl
import com.yjotdev.accidentreporter.utils.repositories.FakeTokenRepositoryImpl

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DiModules::class] // Nombre del módulo real
)
@Suppress("unused")
abstract class DiModulesTest {

    // --- BINDINGS (Abstracciones) ---
    @Binds
    @Singleton
    abstract fun bindFakeReportApiRepository(
        impl: FakeReportApiRepositoryImpl
    ): ReportApiRepository

    @Binds
    @Singleton
    abstract fun bindFakeReportDaoRepository(
        impl: FakeReportDaoRepositoryImpl
    ): ReportDaoRepository

    @Binds
    @Singleton
    abstract fun bindFakeTokenRepository(
        impl: FakeTokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    abstract fun bindFakeStringRepository(
        impl: FakeStringRepositoryImpl
    ): StringRepository

    @Binds
    @Singleton
    abstract fun bindFakeGeocodingRepository(
        impl: FakeGeocodingRepositoryImpl
    ): GeocodingRepository

    // --- PROVIDERS (Instancias externas) ---
    // No son necesarios aquí
}