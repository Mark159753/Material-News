package com.develop.ui.di

import android.content.Context
import com.develop.common.LanguageHelper
import com.develop.local.preferences.AppSettingsImpl
import com.develop.ui.util.LanguageHelperImpl
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
        appSettings: AppSettingsImpl,
        ): LanguageHelper {
        return LanguageHelperImpl(
            appSettings = appSettings,
            appContext = context
        )
    }
}