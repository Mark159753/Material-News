package com.develop.domain.di

import com.develop.data.repositories.articles.ArticlesRepository
import com.develop.data.repositories.filter.UserFilterRepository
import com.develop.data.repositories.top_headers.TopHeaderRepository
import com.develop.domain.usecase.FetchArticlesUC
import com.develop.domain.usecase.FetchTopHeadersUC
import com.develop.local.preferences.AppSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideFetchArticlesUC(
        articlesRepository: ArticlesRepository,
        userFilterRepository: UserFilterRepository,
    ): FetchArticlesUC {
        return FetchArticlesUC(
            articlesRepository,
            userFilterRepository,
        )
    }

    @Provides
    fun provideFetchTopHeadersUC(
        topHeaderRepository: TopHeaderRepository,
        appSettings: AppSettings,
    ): FetchTopHeadersUC {
        return FetchTopHeadersUC(
            topHeaderRepository,
            appSettings,
        )
    }
}