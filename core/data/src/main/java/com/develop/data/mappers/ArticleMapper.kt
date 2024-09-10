package com.develop.data.mappers

import com.develop.common.date.toDate
import com.develop.data.models.articles.ArticleModel
import com.develop.data.models.articles.SourceModel
import com.develop.local.entities.ArticleEntity
import com.develop.local.entities.SourceEmbedded
import com.develop.local.entities.TopHeadersEntity
import com.develop.network.models.top_headers.Article
import com.develop.network.models.top_headers.Source

fun Article.toTopHeaderEntity() = TopHeadersEntity(
    url = url,
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = source?.takeIf { !it.id.isNullOrEmpty() }?.toEntity(),
    title = title,
    urlToImage = urlToImage
)

fun Source.toEntity() = SourceEmbedded(
    id = id!!,
    name = name
)

fun TopHeadersEntity.toModel() = ArticleModel(
    url = url,
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt?.toDate(),
    source = source?.toModel(),
    title = title,
    urlToImage = urlToImage
)

fun SourceEmbedded.toModel() = SourceModel(
    id = id,
    name = name
)

/** Articles */

fun Article.toArticleEntity() = ArticleEntity(
    url = url,
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    source = source?.toEntity(),
    title = title,
    urlToImage = urlToImage
)

fun ArticleEntity.toModel() = ArticleModel(
    url = url,
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt?.toDate(),
    source = source?.toModel(),
    title = title,
    urlToImage = urlToImage
)