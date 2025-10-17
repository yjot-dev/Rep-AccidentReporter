package com.yjotdev.accidentreporter.infrastructure.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.port.TokenPort
import com.yjotdev.accidentreporter.infrastructure.adapter.Api
import com.yjotdev.accidentreporter.infrastructure.repositories.ReportRepository
import com.yjotdev.accidentreporter.infrastructure.repositories.TokenRepository

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {

    @Singleton
    @Provides
    fun provideReportRepository(api: Api): ReportPort =
        ReportRepository(api)

    @Singleton
    @Provides
    fun provideTokenRepository(@ApplicationContext context: Context): TokenPort =
        TokenRepository(context)
}