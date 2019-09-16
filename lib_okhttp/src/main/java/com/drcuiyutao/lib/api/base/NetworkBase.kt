package com.drcuiyutao.lib.api.base

import android.util.Log
import com.drcuiyutao.lib.util.HttpDnsUtil
import com.drcuiyutao.lib.util.SSLUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class NetworkBase<T: Any> private constructor(serviceCls: Class<T>?) {
    private var interceptor: OkHttpClientInterceptor? = null
    private val okHttpClient: OkHttpClient
    private val retrofit: Retrofit?
    private val serviceMap: ConcurrentHashMap<String, T>

    private constructor(serviceCls: Class<T>?, service: T?): this(serviceCls) {
        Log.i(TAG, "secondary constructor init")
        if (service != null) {
            val clz = service::class.java.name
            if (!serviceMap.containsKey(clz)) {
                serviceMap[clz] = service
            }
        }
    }

    init {
        Log.i(TAG, "constructor init code block")
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(READ_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor = OkHttpClientInterceptor())

        val dns = HttpDnsUtil.getDns()
        if (dns != null) {
            builder.dns(dns)
        }

        val trustManager = SSLUtil.getX509TM()
        builder.sslSocketFactory(SSLUtil.createSSLSocketFactory(trustManager), trustManager)
            .hostnameVerifier(SSLUtil.TrustAllHostnameVerifier())
        okHttpClient = builder.build()
        var baseUrl: String? = "https://testyxyapi2.drcuiyutao.com/"
        baseUrl?.let {
            if (!baseUrl.endsWith("/")) {
                baseUrl += "/"
            }
        }
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl!!)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(RequestBodyConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        serviceMap = ConcurrentHashMap()
        getServiceByClass(serviceCls)
    }

    private fun getServiceByClass(clz: Class<*>?): T? {
        clz?.let {
            if (clz.isInterface) {
                retrofit?.let {
                    if (!serviceMap.containsKey(clz.name)) {
                        serviceMap[clz.name] = retrofit.create(clz) as T
                    }
                    return serviceMap[clz.name]
                }
            }
        }

        return null
    }

    companion object {
        private const val CONNECT_TIMEOUT: Long = 10
        private const val READ_WRITE_TIMEOUT: Long = 20
        private const val TAG = "NetworkBase"
        private val base = NetworkBase(null, null)

        fun <T> getService(clz: Class<T>): T? {
            return base.getServiceByClass(clz) as T?
        }
    }
}
