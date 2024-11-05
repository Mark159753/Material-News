package com.develop.materialnews.testing.repository

import com.develop.common.constants.AppLanguage
import com.develop.local.preferences.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestAppSettings:AppSettings {

    private val _isDark = MutableStateFlow<Boolean?>(false)
    override val isDarkMode: Flow<Boolean?>
        get() = _isDark

    private val _currentLocale = MutableStateFlow<AppLanguage?>(AppLanguage.EN)
    override val currentLocale: Flow<AppLanguage?>
        get() = _currentLocale

    override suspend fun changeDarkMode(isEnabled: Boolean?) {
        _isDark.value = isEnabled
    }

    override suspend fun setLocale(lng: AppLanguage) {
        _currentLocale.value = lng
    }
}