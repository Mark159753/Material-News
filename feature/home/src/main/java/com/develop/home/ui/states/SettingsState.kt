package com.develop.home.ui.states

import androidx.compose.runtime.Stable
import com.develop.common.LanguageHelper
import com.develop.common.constants.AppLanguage
import com.develop.local.preferences.AppSettings
import com.develop.ui.util.LanguageHelperImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
interface SettingsState {

    val uiState:SettingsUiState

    fun toggleIsDarkTheme(isDark:Boolean)

    fun changeLng(lng:AppLanguage)

}

class SettingsStateImpl(
    private val appSettings: AppSettings,
    private val languageHelper: LanguageHelper,
    private val scope: CoroutineScope
): SettingsState{

    override val uiState: SettingsUiState = SettingsUiState(
        isDarkTheme = appSettings
            .isDarkMode
            .stateIn(
                scope = scope,
                initialValue = null,
                started = SharingStarted.WhileSubscribed(5_000)
            ),
        currentLng = appSettings
            .currentLocale
            .stateIn(
                scope = scope,
                initialValue = null,
                started = SharingStarted.WhileSubscribed(5_000)
            )
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