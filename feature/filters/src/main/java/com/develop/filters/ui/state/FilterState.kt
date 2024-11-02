package com.develop.filters.ui.state

import androidx.compose.runtime.Stable
import com.develop.data.repositories.filter.UserFilterRepository
import com.develop.data.repositories.source.SourceRepository
import com.develop.ui.util.actions.CommonAction
import com.develop.ui.util.actions.UIActions
import com.develop.ui.util.actions.UIActionsImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
interface FilterState:ChangeState, UIActions {
    val sortByState:SortByState
    val sourceState:SourceState

    fun onSaveChanges()
}

class FilterStateImpl(
    private val userFilterRepository: UserFilterRepository,
    sourceRepository: SourceRepository,
    private val scope:CoroutineScope,
    uiActions: UIActions = UIActionsImpl()
):FilterState,
    ChangeState by ChangeStateImpl(),
        OnChange,
        UIActions by uiActions
{

    private val savedFilters = userFilterRepository
        .filters
        .stateIn(
            scope = scope,
            initialValue = null,
            started = SharingStarted.Eagerly
        )

    private val saverSortBy = savedFilters
        .mapNotNull { item -> item?.sortBy }

    private val savedSources = savedFilters.mapNotNull { item -> item?.sources }

    override val sortByState:SortByState = SortByStateImpl(
        initSortBy = saverSortBy,
        scope = scope,
        onChange = this,
    )

    override val sourceState = SourceStateImpl(
        initSources = sourceRepository.sources,
        scope = scope,
        initSelectedSources = savedSources,
        onChange = this,
        sourceRepository = sourceRepository,
        uiActions = this
    )

    override fun onSaveChanges() {
        scope.launch {
            userFilterRepository.updatePreferences {
                setSortOrder(sortBy = sortByState.sortBy.value)
                setSources(sourceState.selectedSources.value)
            }
            sendAction(CommonAction.NavBack)
        }
    }

    override fun onChange() {
        checkHaveAnyChanges(sourceState, sortByState)
    }
}
