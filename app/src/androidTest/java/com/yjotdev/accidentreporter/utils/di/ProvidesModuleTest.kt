package com.yjotdev.accidentreporter.utils.di

import android.content.Context
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.port.TokenPort
import com.yjotdev.accidentreporter.infrastructure.di.ProvidesModule
import com.yjotdev.accidentreporter.utils.repositories.FakeReportRepositoryImpl
import com.yjotdev.accidentreporter.utils.repositories.FakeTokenRepositoryImpl

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProvidesModule::class] // Nombre del módulo real
)
object ProvidesModuleTest {

    @Singleton
    @Provides
    fun provideFakeReportRepositoryImpl(): ReportPort =
        FakeReportRepositoryImpl()

    @Singleton
    @Provides
    fun provideFakeTokenRepositoryImpl(): TokenPort =
        FakeTokenRepositoryImpl()

    @Singleton
    @Provides
    fun provideTestNavHostController(@ApplicationContext context: Context) =
        TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
}