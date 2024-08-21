package com.develop.materialnews

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.develop.ui.components.news_img.GraphMetaDataProvider
import com.develop.ui.components.news_img.GraphMetaDataProviderImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MaterialNewsApp:Application(),
    ImageLoaderFactory,
    GraphMetaDataProvider by GraphMetaDataProviderImpl()
{

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(false)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .memoryCache {
                MemoryCache.Builder(applicationContext)
                    .maxSizePercent(0.2)
                    .build()
            }
            .respectCacheHeaders(false)
            .build()
    }
}