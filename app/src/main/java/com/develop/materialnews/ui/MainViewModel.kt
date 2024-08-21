package com.develop.materialnews.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.local.preferences.AppSettings
import com.develop.ui.util.LanguageHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appSettings: AppSettings,
    private val languageHelper: LanguageHelper
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