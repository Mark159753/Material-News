package com.develop.data.repositories.top_headers

import com.develop.data.models.articles.ArticleModel
import com.develop.network.until.GenericNetworkResponse
import kotlinx.coroutines.flow.Flow

interface TopHeaderRepository {

    val topHeaders:Flow<List<ArticleModel>>

    suspend fun fetchTopHeaders(country:String):GenericNetworkResponse<List<ArticleModel>>
}