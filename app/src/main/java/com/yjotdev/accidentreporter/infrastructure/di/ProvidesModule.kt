package com.yjotdev.accidentreporter.infrastructure.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.ReportRepository
import com.yjotdev.accidentreporter.domain.port.TokenRepository
import com.yjotdev.accidentreporter.infrastructure.adapter.Api
import com.yjotdev.accidentreporter.infrastructure.repositories.ReportRepositoryImpl
import com.yjotdev.accidentreporter.infrastructure.repositories.TokenRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {

    @Singleton
    @Provides
    fun provideReportRepositoryImpl(api: Api): ReportRepository =
        ReportRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideTokenRepositoryImpl(@ApplicationContext context: Context): TokenRepository =
        TokenRepositoryImpl(context)
}