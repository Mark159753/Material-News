package com.develop.network

import com.develop.network.models.sources.SourcesResponse
import com.develop.network.models.top_headers.TopHeadersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    suspend fun getTopHeaders(
        @Query("country") country:String
    ):Response<TopHeadersResponse>

    @GET("everything")
    suspend fun getEverything(
        @Query("sortBy") sortBy:String? = null,
        @Query("domains") domains:String? = "bbc.co.uk",
        @Query("sources") sources:String? = null,
        @Query("q") q:String? = null,
        @Query("from") from:String? = null,
        @Query("to") to:String? = null,
        @Query("pageSize") pageSize:Int? = null,
        @Query("page") page:Int? = null,
    ):Response<TopHeadersResponse>

    @GET("top-headlines/sources")
    suspend fun getSources():Response<SourcesResponse>
}