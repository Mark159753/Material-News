package com.develop.filters.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.data.repositories.filter.UserFilterRepository
import com.develop.data.repositories.source.SourceRepository
import com.develop.filters.ui.state.FilterState
import com.develop.filters.ui.state.FilterStateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    userFilterRepository: UserFilterRepository,
    private val sourceRepository: SourceRepository,
):ViewModel(){

    val state: FilterState = FilterStateImpl(
        userFilterRepository = userFilterRepository,
        sourceRepository = sourceRepository,
        scope = viewModelScope
    )
}