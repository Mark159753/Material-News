package com.develop.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.develop.local.entities.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao:BaseDao<ArticleEntity> {

    @Query("SELECT * FROM articles")
    suspend fun getAllItems():List<ArticleEntity>

    @Query("SELECT * FROM articles")
    override fun getAllPadding(): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM articles WHERE url = :url")
    suspend fun getItemByUrl(url:String): ArticleEntity?

    @Query("SELECT * FROM articles")
    fun getAllFlow(): Flow<List<ArticleEntity>>

    @Query("DELETE FROM articles")
    override suspend fun deleteAllItems()
}