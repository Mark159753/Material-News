package com.develop.data.models.articles

import java.util.Date


data class ArticleModel(
    val url: String,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: Date?,
    val source: SourceModel?,
    val title: String?,
    val urlToImage: String?
)

data class SourceModel(
    val id:String,
    val name:String
)

fun generateFakeArticleModel() = ArticleModel(
    url = "http://www.smith-cooley.com/",
    author = "Rick Orr",
    content = "Spring wrong teacher. Leave want vote record. Attorney yet minute act degree.",
    description = "Reduce support half such push. ; widojdw jjd ;d ;jwd dw huhw pj opijoip djoipdjw oipjds iopjdsij iodj poijds iojds iojd spisdj poidsj poids phpds ",
    publishedAt = Date(),
    source = SourceModel(
        id = "aada528d-ca9c-4bc3-bd6b-f2750c19b74a",
        name = "King PLC"
    ),
    title = "Re-contextualized high-level emulation",
    urlToImage = "https://placeimg.com/1000/809/any"
)
