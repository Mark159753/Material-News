package com.develop.data.repositories.source

import com.develop.data.models.spurces.SourceModel
import com.develop.network.until.GenericNetworkResponse
import kotlinx.coroutines.flow.Flow

interface SourceRepository{

    val sources:Flow<List<SourceModel>>

    suspend fun fetchSources():GenericNetworkResponse<List<SourceModel>>

}