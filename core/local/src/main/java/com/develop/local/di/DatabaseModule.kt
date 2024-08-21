package com.develop.local.di

import android.content.Context
import androidx.room.Room
import com.develop.local.LocalDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocalDb {
        return Room.databaseBuilder(context, LocalDb::class.java, "local-db")
            .fallbackToDestructiveMigration()
            .build()
    }
}