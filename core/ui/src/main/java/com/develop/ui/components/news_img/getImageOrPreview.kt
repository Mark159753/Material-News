package com.develop.ui.components.news_img

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import java.net.URL


@Composable
fun getImageOrPreview(
    img:String?,
    source:String
): String? {
    val context = LocalContext.current

    val graphProvider = remember {
        (context.applicationContext as GraphMetaDataProvider).provideOpenGraphMetaDataProvider()
    }

    var metadata:OpenGraphMetaData? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = Unit) {
        if (img.isNullOrBlank()){
            val result = graphProvider.startFetchingMetadataAsync(URL(source))
            metadata = result.getOrNull()
        }
    }

    return img ?: metadata?.imageUrl
}