package com.develop.ui.di

import android.content.Context
import com.develop.local.preferences.AppSettings
import com.develop.ui.util.LanguageHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiModule {

    @Provides
    @Singleton
    fun provideLanguageHelper(
        @ApplicationContext context: Context,
        appSettings: AppSettings,
        ): LanguageHelper {
        return LanguageHelper(
            appSettings = appSettings,
            appContext = context
        )
    }
}