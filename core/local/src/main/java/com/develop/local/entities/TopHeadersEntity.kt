package com.develop.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_headers")
data class TopHeadersEntity(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    @Embedded("_source")
    val source: SourceEmbedded?,
    val title: String?,
    val urlToImage: String?
)

data class SourceEmbedded(
    val id:String,
    val name: String
)
