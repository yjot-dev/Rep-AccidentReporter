package com.yjotdev.accidentreporter.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit
import com.yjotdev.accidentreporter.data.remote.api.GeocodingApi
import com.yjotdev.accidentreporter.data.remote.api.ReportApi
import com.yjotdev.accidentreporter.data.remote.network.RetrofitBuilder
import com.yjotdev.accidentreporter.data.local.database.AriDatabase
import com.yjotdev.accidentreporter.data.local.dao.ReportDao
import com.yjotdev.accidentreporter.data.repository.GeocodingRepositoryImpl
import com.yjotdev.accidentreporter.data.repository.ReportApiRepositoryImpl
import com.yjotdev.accidentreporter.data.repository.ReportDaoRepositoryImpl
import com.yjotdev.accidentreporter.data.repository.StringRepositoryImpl
import com.yjotdev.accidentreporter.data.repository.TokenRepositoryImpl
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository
import com.yjotdev.accidentreporter.domain.repository.ReportDaoRepository
import com.yjotdev.accidentreporter.domain.repository.StringRepository
import com.yjotdev.accidentreporter.domain.repository.TokenRepository

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DiModules {

    // --- BINDINGS (Abstracciones) ---
    @Binds
    @Singleton
    abstract fun bindReportApiRepository(
        impl: ReportApiRepositoryImpl
    ): ReportApiRepository

    @Binds
    @Singleton
    abstract fun bindReportDaoRepository(
        impl: ReportDaoRepositoryImpl
    ): ReportDaoRepository

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

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AriDatabase =
            Room.databaseBuilder(
                context,
                AriDatabase::class.java,
                AriDatabase.NAME
            ).fallbackToDestructiveMigration(true).build()

        @Provides
        @Singleton
        fun provideUserDao(database: AriDatabase): ReportDao =
            database.reportDao()
    }
}