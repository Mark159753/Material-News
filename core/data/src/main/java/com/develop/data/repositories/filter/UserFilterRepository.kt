package com.develop.data.repositories.filter

import com.develop.data.models.filters.FilterPreferencesModel
import com.develop.data.models.filters.SortBy
import kotlinx.coroutines.flow.Flow

interface UserFilterRepository {

    val filters:Flow<FilterPreferencesModel>

    suspend fun updatePreferences(block:FilterPreferencesScope.()->Unit)

}

interface FilterPreferencesScope{

    fun setSortOrder(sortBy: SortBy)

    fun setSources(source:Set<String>)
}