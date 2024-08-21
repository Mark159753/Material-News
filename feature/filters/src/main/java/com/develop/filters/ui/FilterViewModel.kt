package com.develop.filters.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.common.result.Result
import com.develop.data.repositories.filter.UserFilterRepository
import com.develop.data.repositories.source.SourceRepository
import com.develop.filters.ui.state.FilterState
import com.develop.filters.ui.state.FilterStateImpl
import com.develop.ui.util.actions.CommonAction
import com.develop.ui.util.actions.UIActions
import com.develop.ui.util.actions.UIActionsImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val userFilterRepository: UserFilterRepository,
    private val sourceRepository: SourceRepository,
):ViewModel(),
    UIActions by UIActionsImpl(){

    val state: FilterState = FilterStateImpl(
        userFilterRepository = userFilterRepository,
        sourceRepository = sourceRepository,
        scope = viewModelScope
    )

    init {
        fetchSources()
    }

    fun onSaveChanges(){
        viewModelScope.launch {
            userFilterRepository.updatePreferences {
                setSortOrder(sortBy = state.sortByState.sortBy.value)
                setSources(state.sourceState.selectedSources.value)
            }
            sendAction(CommonAction.NavBack)
        }
    }

    private fun fetchSources(){
        viewModelScope.launch {
            val response = sourceRepository.fetchSources()
            if (response is Result.Error){
                handleApiError(response.error)
            }
        }
    }
}