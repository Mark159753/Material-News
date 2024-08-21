package com.develop.network.models.top_headers


import com.google.gson.annotations.SerializedName

data class TopHeadersResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("totalResults")
    val totalResults: Int
)