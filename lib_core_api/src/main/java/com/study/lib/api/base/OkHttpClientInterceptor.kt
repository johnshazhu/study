package com.study.lib.api.base

import com.study.lib.api.APISchemeAuthorityConfig
import com.study.lib.api.BaseRequestData
import com.study.lib.api.base.RetrofitBase.Companion.getFormBody
import com.study.lib.log.LogUtil.debug
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class OkHttpClientInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url().toString()
        val scheme = request.url().scheme()
        var sub = url.substring(scheme.length + 3)
        var needReplace = false
        if (sub.contains("//")) {
            needReplace = true
            sub = sub.replace("//", "/")
        }
        if (needReplace) {
            val builder = request.newBuilder()
            builder.url("$scheme://$sub")
            request = builder.build()
        }
        val body = request.body()
        if ((body == null || body.contentLength() == 0L) && RetrofitBase.NO_DEFAULT_BODY != request.tag()
                && request.method() == "POST") {
            val builder = request.newBuilder()
            builder.method(request.method(), getFormBody(BaseRequestData.instance))
            request = builder.build()
        }
        return makeRequest(chain, request)!!
    }

    private fun switchNewAuthority(request: Request): Request {
        var request = request
        val newAuthority: String? = APISchemeAuthorityConfig.instance.getNewSchemeAuthority(getUrlAuthorityByRequest(request))
        newAuthority?.isNotEmpty()?.let {
            val builder = request.newBuilder()
            builder.url(request.url().toString().replace(getUrlAuthorityByRequest(request), newAuthority))
            request = builder.build()
        }
        return request
    }

    private fun getUrlAuthorityByRequest(request: Request): String {
        return request.url().scheme() + "://" + request.url().host()
    }

    @Throws(IOException::class)
    private fun makeRequest(chain: Interceptor.Chain, request: Request): Response? {
        var request = request
        var retry = true
        val isNeedSwitch = APISchemeAuthorityConfig.sSwitchAllApiSchemeAuthority
        var curSchemeAuthority: String? = null
        var newSchemeAuthority: String? = null
        var rsp: Response? = null
        var count = 0
        var newRequest = request
        while (retry) {
            try {
                if (isNeedSwitch) {
                    //该域名在内存Map中对应的状态是--->请求时要进行域名切换
                    curSchemeAuthority = getUrlAuthorityByRequest(request)
                    if (APISchemeAuthorityConfig.instance.isNeedSwitch(curSchemeAuthority)) {
                        newRequest = switchNewAuthority(request)
                        if (newRequest != request) {
                            //更新request, 设置新域名
                            request = newRequest
                            newSchemeAuthority = getUrlAuthorityByRequest(newRequest)
                        }
                        if (newSchemeAuthority != null) {
                            //更新当前请求时所用的域名
                            curSchemeAuthority = newSchemeAuthority
                        }
                    }
                    rsp = chain.proceed(request)
                    retry = false
                    debug("makeRequest success url : " + request.url())
                } else {
                    rsp = chain.proceed(newRequest)
                    retry = false
                }
            } catch (e: IOException) {
                if (RetrofitBase.CANCELED == e.message) {
                    throw e
                }
                count++
                if (isNeedSwitch) {
                    if (count == DEFAULT_MAX_RETRIES) {
                        APISchemeAuthorityConfig.instance.setSchemeAuthoritySwitchFlag(curSchemeAuthority)
                        newRequest = switchNewAuthority(request)
                        if (newRequest != request) {
                            request = newRequest
                        }
                    } else if (count == 2 * DEFAULT_MAX_RETRIES) {
                        throw e
                    }
                } else if (count == DEFAULT_MAX_RETRIES) {
                    throw e
                }
            } catch (r: Throwable) {
                count++
                retry = false
                r.printStackTrace()
                throw IOException("makeRequest throwable")
            }
        }
        return rsp
    }

    companion object {
        //修改重试次数, 切换域名，每个域名重试一次
        private const val DEFAULT_MAX_RETRIES = 2
    }
}