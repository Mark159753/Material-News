package com.develop.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.develop.local.dao.ArticlesDao
import com.develop.local.dao.RemoteKeyDao
import com.develop.local.dao.SourceDao
import com.develop.local.dao.TopHeadersDao
import com.develop.local.entities.ArticleEntity
import com.develop.local.entities.RemoteKeyEntity
import com.develop.local.entities.SourceEntity
import com.develop.local.entities.TopHeadersEntity

@Database(
    entities = [
        TopHeadersEntity::class,
        ArticleEntity::class,
        RemoteKeyEntity::class,
        SourceEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDb: RoomDatabase() {

    abstract fun getRemoteKeyDao(): RemoteKeyDao
    abstract fun getTopHeadersDao(): TopHeadersDao
    abstract fun getArticlesDao():ArticlesDao
    abstract fun getSourceDao():SourceDao
}