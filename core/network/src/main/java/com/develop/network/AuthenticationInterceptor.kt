package com.develop.network

import com.develop.common.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrlBuilder = chain.request()
            .newBuilder()
            .addHeader("x-api-key", BuildConfig.API_KEY)
        return chain.proceed(newUrlBuilder.build())
    }
}