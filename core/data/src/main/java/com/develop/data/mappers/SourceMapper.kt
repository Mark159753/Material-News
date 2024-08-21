package com.develop.data.mappers

import com.develop.data.models.spurces.SourceModel
import com.develop.local.entities.SourceEntity
import com.develop.network.models.sources.Source

fun Source.toEntity() = SourceEntity(
    id = id,
    category = category,
    country = country,
    description = description,
    language = language,
    name = name,
    url = url
)

fun SourceEntity.toModel() = SourceModel(
    id = id,
    category = category,
    country = country,
    description = description,
    language = language,
    name = name,
    url = url
)