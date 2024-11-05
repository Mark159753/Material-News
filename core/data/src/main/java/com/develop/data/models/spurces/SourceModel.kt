package com.develop.data.models.spurces

data class SourceModel(
    val category: String,
    val country: String,
    val description: String?,
    val id: String,
    val language: String,
    val name: String,
    val url: String
)

fun generateDummyModel(index:Int = 0) = SourceModel(
    id = "abc-news_$index",
    name = "ABC News",
    description = "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
    url = "https://abcnews.go.com",
    category = "general",
    language = "en",
    country = "us"
)