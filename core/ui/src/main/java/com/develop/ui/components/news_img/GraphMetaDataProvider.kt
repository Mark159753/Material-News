package com.develop.ui.components.news_img

import com.fresh.materiallinkpreview.parsing.IOpenGraphMetaDataProvider
import com.fresh.materiallinkpreview.parsing.OpenGraphMetaDataProvider

interface GraphMetaDataProvider {

    fun provideOpenGraphMetaDataProvider(): IOpenGraphMetaDataProvider
}

class GraphMetaDataProviderImpl: GraphMetaDataProvider{

    override fun provideOpenGraphMetaDataProvider() = OpenGraphMetaDataProvider()
}