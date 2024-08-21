package com.develop.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val id:String,
    val prevKey:Int?,
    val nextKey:Int?,
    val type:Int
)

const val REMOTE_KEY_ARTICLES= 1
