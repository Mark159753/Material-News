package com.develop.materialnews.testing.repository

import androidx.paging.PagingData
import com.develop.data.models.articles.ArticleModel
import com.develop.data.models.articles.generateFakeArticleModel
import com.develop.data.models.filters.FilterPreferencesModel
import com.develop.data.paging.createTestPagingData
import com.develop.data.repositories.articles.ArticlesRepository
import kotlinx.coroutines.flow.Flow

class TestArticleRepository(
    initList:List<ArticleModel> = List(5){ generateFakeArticleModel(it) }
):ArticlesRepository {

    private val _pagingData = initList.createTestPagingData()
    override fun getArticlesPagingSource(filters: FilterPreferencesModel): Flow<PagingData<ArticleModel>> {
        return _pagingData
    }
}