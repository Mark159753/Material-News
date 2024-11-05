package com.develop.materialnews.testing.repository

import com.develop.data.models.spurces.SourceModel
import com.develop.data.repositories.source.SourceRepository
import com.develop.network.until.GenericNetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestSourceRepository(
    private val initSource:List<SourceModel> = emptyList()
):SourceRepository {

    private val _sourceFlow = MutableStateFlow(initSource)
    override val sources: Flow<List<SourceModel>>
        get() = _sourceFlow

    fun addSources(list: List<SourceModel>){
        _sourceFlow.value = list
    }

    fun clear(){
        _sourceFlow.value = initSource
    }

    override suspend fun fetchSources(): GenericNetworkResponse<List<SourceModel>> {
        return com.develop.common.result.Result.Success(initSource)
    }
}