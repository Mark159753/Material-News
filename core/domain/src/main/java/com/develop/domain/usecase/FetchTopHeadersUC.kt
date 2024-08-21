package com.develop.domain.usecase

import com.develop.data.repositories.top_headers.TopHeaderRepository
import com.develop.local.preferences.AppSettings
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchTopHeadersUC @Inject constructor(
    private val topHeaderRepository: TopHeaderRepository,
    private val appSettings: AppSettings,
) {

    operator fun invoke() = appSettings
        .currentLocale
        .map { lng ->
            topHeaderRepository.fetchTopHeaders(
                country = lng?.country?.uppercase() ?: "US"
            )
        }
}