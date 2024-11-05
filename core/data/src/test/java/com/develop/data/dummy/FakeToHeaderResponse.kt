package com.develop.data.dummy

import com.develop.network.models.top_headers.Article
import com.develop.network.models.top_headers.Source
import com.develop.network.models.top_headers.TopHeadersResponse

fun createFakeTopHeaderResponse(size:Int = 1) = TopHeadersResponse(
    articles = List(size){ createFakeArticle(it) },
    totalResults = size
)

fun createFakeArticle(index:Int = 0) = Article(
    author = "author_$index",
    content = "content_$index",
    description = "descriptions_$index",
    publishedAt = "2024-11-01T17:39:39Z",
    source = Source(
        id = "$index",
        name = "source_name_$index"
    ),
    title = "title_$index",
    url = "url_$index",
    urlToImage = null
)