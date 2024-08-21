package com.develop.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.develop.data.repositories.top_headers.TopHeaderRepository
import com.develop.domain.usecase.FetchArticlesUC
import com.develop.domain.usecase.FetchTopHeadersUC
import com.develop.home.ui.states.SettingsState
import com.develop.home.ui.states.SettingsStateImpl
import com.develop.home.ui.states.TopHeaderState
import com.develop.home.ui.states.TopHeaderStateImpl
import com.develop.local.preferences.AppSettings
import com.develop.ui.util.LanguageHelper
import com.develop.ui.util.actions.UIActions
import com.develop.ui.util.actions.UIActionsImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    topHeaderRepository: TopHeaderRepository,
    appSettings: AppSettings,
    languageHelper: LanguageHelper,
    fetchArticlesUC: FetchArticlesUC,
    fetchTopHeadersUC: FetchTopHeadersUC
):ViewModel(), UIActions by UIActionsImpl() {

    val topHeaderState: TopHeaderState = TopHeaderStateImpl(
        repository = topHeaderRepository,
        scope = viewModelScope,
        actions = this,
        fetchTopHeadersUC
    )

    val settingsState: SettingsState = SettingsStateImpl(
        appSettings = appSettings,
        languageHelper = languageHelper,
        scope = viewModelScope
    )

    val articlesPaging = fetchArticlesUC()
        .cachedIn(viewModelScope)
}