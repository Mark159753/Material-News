package com.develop.data.di

import com.develop.data.repositories.articles.ArticlesRepository
import com.develop.data.repositories.articles.ArticlesRepositoryImpl
import com.develop.data.repositories.filter.UserFilterRepository
import com.develop.data.repositories.filter.UserFilterRepositoryImpl
import com.develop.data.repositories.source.SourceRepository
import com.develop.data.repositories.source.SourceRepositoryImpl
import com.develop.data.repositories.top_headers.TopHeaderRepository
import com.develop.data.repositories.top_headers.TopHeaderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTopHeaderRepository(
        repository: TopHeaderRepositoryImpl
    ): TopHeaderRepository

    @Binds
    abstract fun bindArticlesRepository(
        repository: ArticlesRepositoryImpl
    ): ArticlesRepository

    @Binds
    abstract fun bindUserFilterRepository(
        repository: UserFilterRepositoryImpl
    ): UserFilterRepository


    @Binds
    abstract fun bindSourceRepository(
        repository: SourceRepositoryImpl
    ): SourceRepository
}