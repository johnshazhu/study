package com.study.lib.api.base

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException

class OkHttpClientInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        Log.i("xdebug", request.url().toString())

        return chain.proceed(request)
    }
}
