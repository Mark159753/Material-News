package com.develop.home.ui.states

import androidx.compose.runtime.Stable
import com.develop.common.result.Result
import com.develop.data.models.articles.ArticleModel
import com.develop.data.repositories.top_headers.TopHeaderRepository
import com.develop.domain.usecase.FetchTopHeadersUC
import com.develop.ui.util.actions.UIActions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
interface TopHeaderState {

    val items:StateFlow<List<ArticleModel?>>

    fun fetchTopHeaders()
}

class TopHeaderStateImpl(
    repository: TopHeaderRepository,
    private val scope:CoroutineScope,
    private val actions:UIActions,
    private val fetchTopHeadersUC: FetchTopHeadersUC
): TopHeaderState {

    override val items: StateFlow<List<ArticleModel?>> = repository
        .topHeaders
        .map { list ->
            list.take(12)
        }
        .stateIn(
            scope = scope,
            initialValue = List(5) { null },
            started = SharingStarted.WhileSubscribed(5_000)
        )

    init {
        fetchTopHeaders()
    }

    override fun fetchTopHeaders() {
        scope.launch {
            fetchTopHeadersUC()
                .collectLatest { response ->
                    if (response is Result.Error){
                        actions.handleApiError(response.error)
                    }
                }
        }
    }
}
