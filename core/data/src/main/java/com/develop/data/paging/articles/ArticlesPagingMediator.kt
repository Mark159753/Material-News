package com.develop.data.paging.articles

import com.develop.common.result.Result
import com.develop.data.mappers.toArticleEntity
import com.develop.data.models.filters.FilterPreferencesModel
import com.develop.data.paging.base.BasePagingMediator
import com.develop.local.LocalDb
import com.develop.local.entities.ArticleEntity
import com.develop.local.entities.REMOTE_KEY_ARTICLES
import com.develop.local.entities.RemoteKeyEntity
import com.develop.network.ApiService
import com.develop.network.models.top_headers.Article
import com.develop.network.models.top_headers.TopHeadersResponse
import com.develop.network.until.GenericNetworkResponse
import com.develop.network.until.safeApiCall

class ArticlesPagingMediator(
    private val db: LocalDb,
    private val apiService: ApiService,
    private val filters: FilterPreferencesModel
): BasePagingMediator<ArticleEntity, Article>(db.getArticlesDao(), db.getRemoteKeyDao(), REMOTE_KEY_ARTICLES) {

    
    override suspend fun apiCall(page: Int): GenericNetworkResponse<List<Article>> {
        val response:GenericNetworkResponse<TopHeadersResponse> = safeApiCall {
            apiService.getEverything(
                page = page,
                sortBy = filters.sortBy.name.lowercase(),
                sources = filters.sources.joinToString()
                )
        }
        return when(response){
            is Result.Error -> response
            is Result.Success -> Result.Success(response.data.articles)
        }
    }

    override fun toRemoteKey(
        prevKey: Int?,
        nextKey: Int?,
        item: Article
    ) = RemoteKeyEntity("${item.url}_$REMOTE_KEY_ARTICLES", prevKey, nextKey, REMOTE_KEY_ARTICLES)

    override fun toEntity(item: Article) = item.toArticleEntity()


    override suspend fun findRemoteKey(
        item: ArticleEntity?
    ) = item?.url?.let { db.getRemoteKeyDao().getItemById("${item.url}_$REMOTE_KEY_ARTICLES") }
}