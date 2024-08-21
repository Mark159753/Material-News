package com.develop.data.models.filters

data class FilterPreferencesModel(
    val sortBy:SortBy = SortBy.POPULARITY,
    val sources:Set<String> = emptySet()
)

enum class SortBy{
    RELEVANCY,
    POPULARITY,
    PUBLISHED_AT,
}
