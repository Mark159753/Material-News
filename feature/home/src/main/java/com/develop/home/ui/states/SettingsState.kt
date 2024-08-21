package com.develop.home.ui.states

import androidx.compose.runtime.Stable
import com.develop.common.constants.AppLanguage
import com.develop.local.preferences.AppSettings
import com.develop.ui.util.LanguageHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
interface SettingsState {

    val isDarkTheme:StateFlow<Boolean?>

    val currentLng:StateFlow<AppLanguage?>

    fun toggleIsDarkTheme(isDark:Boolean)

    fun changeLng(lng:AppLanguage)

}

class SettingsStateImpl(
    private val appSettings: AppSettings,
    private val languageHelper: LanguageHelper,
    private val scope: CoroutineScope
): SettingsState{

    override val isDarkTheme: StateFlow<Boolean?> = appSettings
        .isDarkMode
        .stateIn(
            scope = scope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    override val currentLng: StateFlow<AppLanguage?> = appSettings
        .currentLocale
        .stateIn(
            scope = scope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    override fun toggleIsDarkTheme(isDark:Boolean) {
        scope.launch {
            appSettings.changeDarkMode(isDark)
        }
    }

    override fun changeLng(lng: AppLanguage) {
        scope.launch {
            languageHelper.changeLanguage(lng)
        }
    }
}