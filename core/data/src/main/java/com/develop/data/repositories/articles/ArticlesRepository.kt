package com.develop.data.repositories.articles

import androidx.paging.PagingData
import com.develop.data.models.articles.ArticleModel
import com.develop.data.models.filters.FilterPreferencesModel
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {

    fun getArticlesPagingSource(filters:FilterPreferencesModel): Flow<PagingData<ArticleModel>>
}