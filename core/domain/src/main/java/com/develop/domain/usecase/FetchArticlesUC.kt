package com.develop.domain.usecase

import androidx.paging.PagingData
import com.develop.data.models.articles.ArticleModel
import com.develop.data.repositories.articles.ArticlesRepository
import com.develop.data.repositories.filter.UserFilterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchArticlesUC @Inject constructor(
    private val articlesRepository: ArticlesRepository,
    private val userFilterRepository: UserFilterRepository,
) {

    operator fun invoke():Flow<PagingData<ArticleModel>> = userFilterRepository
        .filters
        .flatMapLatest { filters ->
            articlesRepository.getArticlesPagingSource(filters)
        }
        .flowOn(Dispatchers.IO)
}