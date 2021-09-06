package com.study.lib.api.base

import android.text.TextUtils
import android.util.Log
import com.study.lib.api.APIUtils.type
import com.study.lib.json.JsonUtil
import com.study.lib.util.InjectUtil
import com.study.lib.util.LockTimer
import com.study.lib.util.LockTimer.LockTimerCallback
import com.study.lib.log.LogUtil.debug
import com.study.lib.util.ToastUtil
import com.study.lib.util.ToastUtil.show
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.study.lib.R
import com.study.lib.api.*
import com.study.lib.ui.UIController
import com.study.lib.ui.UIUpdateListener
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

/**
 * Created by john on 2018/10/18.
 */
object ResponseHandlerUtil {
    @JvmStatic
    var isWxNoUnionId = false
        private set
    private val mSkipClass: HashSet<Class<*>> = object : HashSet<Class<*>>() {
        init {
            add(LockTimer::class.java)
            add(LockTimerCallback::class.java)
        }
    }
    private val sExclusionStrategy = exclusionStrategy
    private val exclusionStrategy: ExclusionStrategy
        private get() = object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return mSkipClass.contains(f.declaredClass)
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }
        }

    @JvmStatic
    fun addSkipClass(cls: Class<*>) {
        mSkipClass.add(cls)
    }

    @Throws(IOException::class)
    fun <T : BaseResponseData> parseResponse(call: Call, response: Response,
                                             result: String,
                                             request: APIBaseRequest<T>
    ) {
        if (request.rspListener == null) {
            return
        }

        if (response.isSuccessful) {
            parseResponse(result, request, request.rspListener, request.controller, request.uiListener)
        } else {
            parseFailure(call, response, result, request, null)
        }
    }

    @JvmStatic
    fun resetWxNoUnionIdFlag() {
        isWxNoUnionId = false
    }

    private fun <T : BaseResponseData> parse(result: String, request: APIBaseRequest<T>): APIBaseResponse<T>? {
        var rsp: APIBaseResponse<T>? = null
        if (!TextUtils.isEmpty(result)) {
            debug("parse result : " + result + ", url : " + request.url)
            try {
                val type = request.javaClass.genericSuperclass
                val objectType: Type
                var targetType: Type?
                if (request is TransformRequest) {
                    targetType = request.genericType
                    objectType = type(APIBaseResponse::class.java, targetType!!)
                } else {
                    objectType = type(
                        APIBaseResponse::class.java,
                            (type as ParameterizedType).actualTypeArguments[0].also {
                                targetType = it
                            })
                }
                val gson: Gson = JsonUtil.gsonBuilder.setExclusionStrategies(sExclusionStrategy).create()
                rsp = gson.fromJson(result, objectType)
                if (rsp != null && rsp.data == null) {
                    val targetObj = Gson().fromJson<Any>("{}", targetType)
                    if (targetObj is BaseResponseData) {
                        rsp.data = targetObj as T
                    }
                }
            } catch (r: Throwable) {
                r.printStackTrace()
            }
        }
        if (rsp == null) {
            Log.e("ResponseHandler", "null rsp")
        }
        return rsp
    }

    @Throws(IOException::class)
    private fun <T : BaseResponseData> parseResponse(result: String,
                                                     request: APIBaseRequest<T>,
                                                     listener: APIBase.ResponseListener<T>?,
                                                     controller: UIController?,
                                                     uiListener: UIUpdateListener?) {
        val rsp = parse(result, request)
        if (rsp == null) {
            exceptionProcess(controller, uiListener)
            return
        }
        if (rsp.isWxNoUnionID && !isWxNoUnionId) {
            isWxNoUnionId = true
            InjectUtil.forceLogOut(rsp.msg, true)
            return
        }
        val isTokenExpired: Boolean = InjectUtil.parseRouters(rsp.routers)
        if (isTokenExpired) {
            controller?.stop(true)
            return
        }
        val isBusinessSuccess = rsp.isSuccess
        if (controller != null) {
            if (isBusinessSuccess) {
                if (request.subRequest != null) {
                    request.subRequest!!.controller = controller
                    controller.isCloseLoadingWhenFinished = false
                } else if (controller.isChanged) {
                    controller.isCloseLoadingWhenFinished = true
                }
            } else if (controller.isCloseLoadingWhenFinished) {
                controller.toastMsg = rsp.msg
            }
            controller.stop(!isBusinessSuccess)
        }
        request.response = rsp
        if (uiListener != null) {
            if (isBusinessSuccess && rsp.isEmpty) {
                uiListener.showEmptyContentView()
            } else if (!isBusinessSuccess) {
                if (rsp.isBusinessError) {
                    uiListener.showBusinessErrorView(rsp.getBscode(), rsp.msg)
                } else {
                    uiListener.showConnectExceptionView(true)
                }
            } else {
                uiListener.hideRefreshView()
                request.subRequest?.postSubRequest(request.subRspListener)
                listener!!.onSuccess(rsp.data!!, result, rsp.getBscode(), rsp.msg, true)
            }
        } else {
            listener?.onSuccess(rsp.data!!, result, rsp.getBscode(), rsp.msg, isBusinessSuccess)
        }
        try {
            request.onSuccess(request)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    @Throws(IOException::class)
    fun <T : BaseResponseData> parseFailure(call: Call, response: Response?,
                                            result: String?,
                                            request: APIBaseRequest<T>,
                                            exception: IOException?) {
        var result = result
        val listener = request.rspListener ?: return
        val controller = request.controller
        val uiListener: UIUpdateListener? = request.uiListener
        val rsp: APIBaseResponse<T>
        if (exception != null) {
            listener.onFailureWithException(request.url, exception)
            val type = request.javaClass.genericSuperclass
            if (result != null) {
                try {
                    val objectType: Type = type(
                        APIBaseResponse::class.java,
                            (type as ParameterizedType).actualTypeArguments[0])
                    val gson: Gson = JsonUtil.gsonBuilder.setExclusionStrategies(sExclusionStrategy).create()
                    rsp = gson.fromJson(result, objectType)
                    if (rsp != null && rsp.isServerStopped && controller != null) {
                        show(if (TextUtils.isEmpty(rsp.msg)) "服务不可用" else rsp.msg,
                                ToastUtil.TOAST_DURATION)
                        controller.isShowToastWhenFailed = false
                    }
                } catch (r: Throwable) {
                    r.printStackTrace()
                }
            }
            try {
                request.onFail(request)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        if (uiListener != null) {
            uiListener.showConnectExceptionView(true)
            uiListener.releaseToRefresh()
        }
        try {
            result = InjectUtil.context()!!.getString(R.string.network_exception)
            if (response != null) {
                debug("onFailure code : ${response.code()}, url : ${call.request().url()}, cause :  ${response.message() ?: "unknown"}")
                listener.onFailure(response.code(), result)
            } else {
                listener.onFailure(0, result)
            }
            if (controller != null) {
                //连接超时提示，服务器错误时不提示
                if (controller.isShowToastWhenFailed) {
                    controller.toastMsg = result
                }
                controller.stop(true)
            }
        } catch (r: Throwable) {
            r.printStackTrace()
        }
    }

    private fun exceptionProcess(controller: UIController?, uiListener: UIUpdateListener?) {
        controller?.stop(true)
        uiListener?.showConnectExceptionView(true)
    }
}