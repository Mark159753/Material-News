package com.develop.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.develop.local.entities.TopHeadersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopHeadersDao:BaseDao<TopHeadersEntity> {

    @Query("SELECT * FROM top_headers")
    suspend fun getAllItems():List<TopHeadersEntity>

    @Query("SELECT * FROM top_headers WHERE url = :url")
    suspend fun getItemByUrl(url:String): TopHeadersEntity?

    @Query("SELECT * FROM top_headers")
    fun getAllFlow(): Flow<List<TopHeadersEntity>>

    @Query("SELECT * FROM top_headers")
    override fun getAllPadding(): PagingSource<Int, TopHeadersEntity>

    @Query("DELETE FROM top_headers")
    override suspend fun deleteAllItems()
}