package com.yjotdev.accidentreporter.utils.di

import dagger.Module
import dagger.Binds
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.port.TokenPort
import com.yjotdev.accidentreporter.infrastructure.di.DiModules
import com.yjotdev.accidentreporter.utils.repositories.FakeReportRepository
import com.yjotdev.accidentreporter.utils.repositories.FakeTokenRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DiModules::class] // Nombre del módulo real
)
@Suppress("unused")
abstract class DiModulesTest {
    @Binds
    @Singleton
    abstract fun bindFakeReportRepository(
        impl: FakeReportRepository
    ): ReportPort

    @Binds
    @Singleton
    abstract fun bindFakeTokenRepository(
        impl: FakeTokenRepository
    ): TokenPort
}