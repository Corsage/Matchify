package com.corsage.memory_matching.network

import com.corsage.memory_matching.util.ApiConstants
import okhttp3.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * OkHttp Interceptor.
 * This adds all common API parameters into every request.
 */

class OkHttpInterceptor : Interceptor {
    private val TAG = "OkHttpInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(ApiConstants.API_ACCESS_TOKEN, "c32313df0d0ef512ca64d5b336a0d7c6")
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}