package com.develop.data.repositories.source

import com.develop.common.result.Result
import com.develop.data.mappers.toEntity
import com.develop.data.mappers.toModel
import com.develop.data.models.spurces.SourceModel
import com.develop.local.LocalDb
import com.develop.network.ApiService
import com.develop.network.models.error.ErrorResponse
import com.develop.network.models.sources.SourcesResponse
import com.develop.network.until.GenericNetworkResponse
import com.develop.network.until.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SourceRepositoryImpl @Inject constructor(
    private val db: LocalDb,
    private val apiService: ApiService
) : SourceRepository {


    override val sources: Flow<List<SourceModel>>
        get() = db.getSourceDao().getAllFlow().map { list ->
            list.map { it.toModel() }
        }

    override suspend fun fetchSources(): GenericNetworkResponse<List<SourceModel>> {
        val response = safeApiCall<SourcesResponse, ErrorResponse> {
            apiService.getSources()
        }
        return when(response){
            is Result.Error -> response
            is Result.Success -> {
                val entities = response.data.sources.map { it.toEntity() }
                db.getSourceDao().insert(entities)
                Result.Success(entities.map { it.toModel() })
            }
        }
    }
}