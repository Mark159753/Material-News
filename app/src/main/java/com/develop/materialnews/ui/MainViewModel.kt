package com.develop.materialnews.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.local.preferences.AppSettingsImpl
import com.develop.ui.util.LanguageHelperImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appSettings: AppSettingsImpl,
    private val languageHelper: LanguageHelperImpl
):ViewModel() {

    val isSystemInDark = appSettings
        .isDarkMode
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    suspend fun initCurrentLocale(context: Context){
        languageHelper.saveInitLng(context)
    }
}