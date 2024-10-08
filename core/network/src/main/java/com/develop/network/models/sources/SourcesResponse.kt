package com.develop.network.models.sources


import com.google.gson.annotations.SerializedName

data class SourcesResponse(
    @SerializedName("sources")
    val sources: List<Source>,
    @SerializedName("status")
    val status: String?
)