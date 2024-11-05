package com.develop.filters.ui.state

import androidx.compose.runtime.Stable
import com.develop.data.models.filters.SortBy
import com.develop.data.models.spurces.SourceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
data class FilterScreenUIState(
    val anyChanges:StateFlow<Boolean> = MutableStateFlow(false),
    val sourcesList:StateFlow<List<SourceModel>> = MutableStateFlow(emptyList()),
    val selectedSources:StateFlow<Set<String>> = MutableStateFlow(emptySet()),
    val sortBy:StateFlow<SortBy> = MutableStateFlow(SortBy.POPULARITY)
)