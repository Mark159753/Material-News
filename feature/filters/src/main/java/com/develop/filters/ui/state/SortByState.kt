package com.develop.filters.ui.state

import androidx.compose.runtime.Stable
import com.develop.data.models.filters.SortBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@Stable
interface SortByState: ChangesComparator {

    val sortBy:StateFlow<SortBy>

    fun onSetSort(sortBy: SortBy)

}

class SortByStateImpl(
    initSortBy:Flow<SortBy>,
    scope:CoroutineScope,
    private val onChange: OnChange,
):SortByState{

    private val initSort = initSortBy
        .stateIn(
            scope = scope,
            initialValue = SortBy.POPULARITY,
            started = SharingStarted.Eagerly
        )

    private val _sortBy = MutableStateFlow<SortBy?>(null)
    override val sortBy: StateFlow<SortBy> = combine(initSort, _sortBy){ initValue, selected ->
        selected ?: initValue
    }
        .stateIn(
            scope = scope,
            initialValue = SortBy.POPULARITY,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    override fun onSetSort(sortBy: SortBy) {
        _sortBy.value = sortBy
        onChange.onChange()
    }


     override fun checkIsChanged() = initSort.value != sortBy.value
}