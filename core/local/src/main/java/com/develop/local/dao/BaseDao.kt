package com.develop.local.dao

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction

interface BaseDao<T:Any> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: List<T>)

    fun getAllPadding(): PagingSource<Int, T>

    suspend fun deleteAllItems()
}