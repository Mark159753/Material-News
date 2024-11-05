package com.develop.home.ui.states

import androidx.compose.runtime.Stable
import com.develop.common.constants.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
data class SettingsUiState(
    val isDarkTheme:StateFlow<Boolean?> = MutableStateFlow(false),
    val currentLng:StateFlow<AppLanguage?> = MutableStateFlow(AppLanguage.EN)
)
