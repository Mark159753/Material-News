package com.develop.filters.ui.state

import androidx.compose.runtime.Stable
import com.develop.data.repositories.filter.UserFilterRepository
import com.develop.data.repositories.source.SourceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn

@Stable
interface FilterState:ChangeState {
    val sortByState:SortByState
    val sourceState:SourceState
}

class FilterStateImpl(
    userFilterRepository: UserFilterRepository,
    sourceRepository: SourceRepository,
    scope:CoroutineScope
):FilterState,
    ChangeState by ChangeStateImpl(),
        OnChange
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
        onChange = this
    )

    override fun onChange() {
        checkHaveAnyChanges(sourceState, sortByState)
    }
}
