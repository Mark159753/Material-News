package com.develop.materialnews.testing.repository

import android.content.Context
import com.develop.common.LanguageHelper
import com.develop.common.constants.AppLanguage
import com.develop.local.preferences.AppSettings

class TestLanguageHelper(
    private val appSettings: AppSettings
): LanguageHelper {

    override suspend fun changeLanguage(locale: AppLanguage) {
        appSettings.setLocale(locale)
    }

    override suspend fun saveInitLng(context: Context) {

    }
}