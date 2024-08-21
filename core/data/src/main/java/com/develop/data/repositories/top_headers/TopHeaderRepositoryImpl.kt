package com.develop.data.repositories.top_headers

import androidx.room.withTransaction
import com.develop.common.result.Result
import com.develop.data.mappers.toModel
import com.develop.data.mappers.toTopHeaderEntity
import com.develop.data.models.articles.ArticleModel
import com.develop.local.LocalDb
import com.develop.network.ApiService
import com.develop.network.models.error.ErrorResponse
import com.develop.network.models.top_headers.TopHeadersResponse
import com.develop.network.until.GenericNetworkResponse
import com.develop.network.until.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TopHeaderRepositoryImpl @Inject constructor(
    private val db: LocalDb,
    private val apiService: ApiService
) : TopHeaderRepository {

    override val topHeaders: Flow<List<ArticleModel>>
        get() = db.getTopHeadersDao().getAllFlow()
            .map { list ->
                list.map { it.toModel() }
            }

    override suspend fun fetchTopHeaders(country:String): GenericNetworkResponse<List<ArticleModel>> {
        val response = safeApiCall<TopHeadersResponse, ErrorResponse> {
            apiService.getTopHeaders(country)
        }
        return when(response){
            is Result.Error -> response
            is Result.Success -> {
                val entities = response.data.articles.map { it.toTopHeaderEntity() }
                db.withTransaction {
                    db.getTopHeadersDao().deleteAllItems()
                    db.getTopHeadersDao().insert(entities)
                }
                Result.Success(entities.map { it.toModel() })
            }
        }
    }
}