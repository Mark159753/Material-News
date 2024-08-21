package com.develop.data.mappers

import com.develop.data.models.filters.FilterPreferencesModel
import com.develop.data.models.filters.SortBy
import com.develop.local.proto.UserFilterPreferences

fun UserFilterPreferences.toModel() = FilterPreferencesModel(
    sortBy = sortOrder.toModel(),
    sources = sourcesList.toSet().ifEmpty { setOf(DEFAULT_SOURCE) }
)

fun UserFilterPreferences.SortOrder.toModel() = try {
    SortBy.valueOf(name)
}catch (e:Exception){
    SortBy.POPULARITY
}

fun SortBy.toPreferences() = try {
    UserFilterPreferences.SortOrder.valueOf(name)
}catch (e:Exception){
    UserFilterPreferences.SortOrder.POPULARITY
}

private const val DEFAULT_SOURCE = "bbc-news"