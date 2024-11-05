package com.develop.data.paging

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun <T : Any> List<T>.createTestPagingData(): Flow<PagingData<T>> {
    return flowOf(PagingData.from(this))
}