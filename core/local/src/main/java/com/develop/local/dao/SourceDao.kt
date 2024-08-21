package com.develop.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.develop.local.entities.SourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao:BaseDao<SourceEntity>  {

    @Query("SELECT * FROM sources_tab")
    suspend fun getAllItems():List<SourceEntity>

    @Query("SELECT * FROM sources_tab")
    override fun getAllPadding(): PagingSource<Int, SourceEntity>

    @Query("SELECT * FROM sources_tab WHERE id = :id")
    suspend fun getItemById(id:String): SourceEntity?

    @Query("SELECT * FROM sources_tab")
    fun getAllFlow(): Flow<List<SourceEntity>>

    @Query("DELETE FROM sources_tab")
    override suspend fun deleteAllItems()
}