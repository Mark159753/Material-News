package com.develop.data.repositories.filter

import android.content.Context
import android.util.Log
import com.develop.data.mappers.toModel
import com.develop.data.mappers.toPreferences
import com.develop.data.models.filters.FilterPreferencesModel
import com.develop.data.models.filters.SortBy
import com.develop.local.preferences.filter.userFilterPreferencesStore
import com.develop.local.proto.UserFilterPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserFilterRepositoryImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : UserFilterRepository {


    override val filters: Flow<FilterPreferencesModel> = context.userFilterPreferencesStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(UserFilterPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
        .map { item -> item.toModel() }


    override suspend fun updatePreferences(block: FilterPreferencesScope.() -> Unit) {
        context.userFilterPreferencesStore.updateData { preferences ->
            val b = preferences.toBuilder()
            val scope = FilterPreferencesScopeImpl(b)
            block.invoke(scope)
            b.build()
        }

    }
}

internal class FilterPreferencesScopeImpl(
    private val builder:UserFilterPreferences.Builder
):FilterPreferencesScope{

    override fun setSortOrder(sortBy: SortBy) {
        builder.setSortOrder(sortBy.toPreferences())
    }

    override fun setSources(source: Set<String>) {
        builder.clearSources()
        builder.addAllSources(source)
    }
}

private const val TAG = "UserFilterRepository"