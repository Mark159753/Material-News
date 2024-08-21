package com.develop.data.repositories.articles

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.develop.data.mappers.toModel
import com.develop.data.models.articles.ArticleModel
import com.develop.data.models.filters.FilterPreferencesModel
import com.develop.data.paging.articles.ArticlesPagingMediator
import com.develop.local.LocalDb
import com.develop.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticlesRepositoryImpl @Inject constructor(
    private val db: LocalDb,
    private val apiService: ApiService,
) : ArticlesRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getArticlesPagingSource(filters: FilterPreferencesModel): Flow<PagingData<ArticleModel>> {
        val pagingSourceFactory = { db.getArticlesDao().getAllPadding() }

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ArticlesPagingMediator(db, apiService, filters),
            pagingSourceFactory = pagingSourceFactory,
        ).flow
            .map { pagingData ->
                pagingData.map { item ->
                    item.toModel()
                }
            }
    }
}

private const val PAGE_SIZE = 20