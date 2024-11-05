package com.develop.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.develop.common.LanguageHelper
import com.develop.common.result.Result
import com.develop.data.repositories.top_headers.TopHeaderRepository
import com.develop.domain.usecase.FetchArticlesUC
import com.develop.domain.usecase.FetchTopHeadersUC
import com.develop.home.ui.states.ArticlesState
import com.develop.home.ui.states.SettingsState
import com.develop.home.ui.states.SettingsStateImpl
import com.develop.local.preferences.AppSettings
import com.develop.ui.util.actions.UIActions
import com.develop.ui.util.actions.UIActionsImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    topHeaderRepository: TopHeaderRepository,
    appSettings: AppSettings,
    languageHelper: LanguageHelper,
    fetchArticlesUC: FetchArticlesUC,
    private val fetchTopHeadersUC: FetchTopHeadersUC
):ViewModel(), UIActions by UIActionsImpl() {


    val articlesSate = ArticlesState(
        topHeaderState = topHeaderRepository
            .topHeaders
            .onStart { fetchTopHeaders() }
            .map { list ->
                list.take(12)
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = List(5) { null },
                started = SharingStarted.WhileSubscribed(5_000)
            ),
        articlesPaging = fetchArticlesUC()
            .cachedIn(viewModelScope)
    )

    val settingsState: SettingsState = SettingsStateImpl(
        appSettings = appSettings,
        languageHelper = languageHelper,
        scope = viewModelScope
    )

    private fun fetchTopHeaders() {
        viewModelScope.launch {
            fetchTopHeadersUC()
                .collectLatest { response ->
                    if (response is Result.Error){
                        handleApiError(response.error)
                    }
                }
        }
    }
}