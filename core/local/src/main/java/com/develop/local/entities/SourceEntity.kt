package com.develop.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources_tab")
data class SourceEntity(
    val category: String,
    val country: String,
    val description: String?,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val language: String,
    val name: String,
    val url: String
)
