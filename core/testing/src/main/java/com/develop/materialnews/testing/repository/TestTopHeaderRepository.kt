package com.develop.materialnews.testing.repository

import com.develop.common.result.Result
import com.develop.data.models.articles.ArticleModel
import com.develop.data.models.articles.generateFakeArticleModel
import com.develop.data.repositories.top_headers.TopHeaderRepository
import com.develop.network.until.GenericNetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestTopHeaderRepository(
    private val initList:List<ArticleModel> = List(5){ generateFakeArticleModel(it) }
):TopHeaderRepository {

    private val _topHeaders = MutableStateFlow(initList)
    override val topHeaders: Flow<List<ArticleModel>>
        get() = _topHeaders

    override suspend fun fetchTopHeaders(country: String): GenericNetworkResponse<List<ArticleModel>> {
        return Result.Success(initList)
    }

    fun clear(){
        _topHeaders.value = emptyList()
    }

    fun setData(list:List<ArticleModel>){
        _topHeaders.value = list
    }
}