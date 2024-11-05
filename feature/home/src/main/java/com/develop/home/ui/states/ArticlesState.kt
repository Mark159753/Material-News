package com.develop.home.ui.states

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.develop.data.models.articles.ArticleModel
import com.develop.data.models.articles.generateFakeArticleModel
import com.develop.data.paging.createTestPagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Stable
data class ArticlesState(
    val topHeaderState:StateFlow<List<ArticleModel?>> = MutableStateFlow(emptyList()),
    val articlesPaging:Flow<PagingData<ArticleModel>> = List(5){ generateFakeArticleModel(it) }.createTestPagingData()
)
