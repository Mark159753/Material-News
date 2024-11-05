package com.develop.materialnews.testing.repository

import com.develop.data.models.filters.FilterPreferencesModel
import com.develop.data.models.filters.SortBy
import com.develop.data.repositories.filter.FilterPreferencesScope
import com.develop.data.repositories.filter.UserFilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestUserFilterRepository(
    private val intFilter:FilterPreferencesModel = FilterPreferencesModel()
): UserFilterRepository {

    private val _filters = MutableStateFlow(intFilter)
    override val filters: Flow<FilterPreferencesModel>
        get() = _filters

    override suspend fun updatePreferences(block: FilterPreferencesScope.() -> Unit) {
        block.invoke(object :FilterPreferencesScope{
            override fun setSortOrder(sortBy: SortBy) {
                _filters.value = _filters.value.copy(sortBy = sortBy)
            }

            override fun setSources(source: Set<String>) {
                _filters.value = _filters.value.copy(sources = source)
            }
        })
    }

    fun clear(){
        _filters.value = intFilter
    }
}