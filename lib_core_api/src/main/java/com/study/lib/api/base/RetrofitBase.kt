package com.study.lib.api.base

import com.study.lib.api.APIConfig1
import com.study.lib.api.APIUtils
import com.study.lib.log.LogUtil.i
import com.study.lib.util.HttpDnsUtil
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class RetrofitBase private constructor() {
    private val okHttpClient: OkHttpClient
    private var interceptor: OkHttpClientInterceptor? = null

    private object RetrofitBaseHolder {
        val base = RetrofitBase()
    }

    companion object {
        const val METHOD_POST = "POST"
        const val NO_DEFAULT_BODY = "NO_DEFAULT_BODY"
        const val CANCELED = "Canceled"
        private const val BODY = "body"
        const val HOST = "Host"

        @JvmStatic
        val client: OkHttpClient
            get() = RetrofitBaseHolder.base.okHttpClient

        @JvmStatic
        lateinit var retrofit: Retrofit

        fun <T> getFormBody(value: T): FormBody {
            val builder = FormBody.Builder()
            builder.add(BODY, APIUtils.updateBodyString(Gson().toJson(value)))
            return builder.build()
        }
    }

    init {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(OkHttpClientInterceptor().also { interceptor = it })
        val dns = HttpDnsUtil.dns
        dns?.let {
            builder.dns(dns)
        }

        val sslSocketFactory = SSLUtil.createSSLSocketFactory()
        sslSocketFactory?.let {
            builder.sslSocketFactory(sslSocketFactory).hostnameVerifier(SSLUtil.TrustAllHostnameVerifier())
        }

        okHttpClient = builder.build()
        var baseUrl = APIConfig1.BASE
        if (baseUrl.isNotEmpty() && !baseUrl.endsWith(File.separator)) {
            baseUrl += File.separator
        }
        i("RetrofitBase", "baseUrl : $baseUrl")
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(RequestBodyConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}