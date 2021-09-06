package com.study.lib.api.base

import android.app.Activity
import android.content.Context
import android.util.Log
import com.study.lib.api.LoadingCancelListener
import com.study.lib.R
import com.study.lib.ui.UIController
import com.study.lib.ui.UIUpdateListener
import com.study.lib.util.InjectUtil
import com.study.lib.util.ToastUtil
import com.study.lib.util.SimpleUtil
import com.study.lib.api.APIBase
import com.study.lib.api.APIBaseResponse
import com.study.lib.api.BaseResponseData
import com.study.lib.api.TransformRequest
import retrofit2.Call

/**
 * Created by john on 2018/10/16.
 */
object RetrofitAPIRequest {
    /*
     * 请求时默认不展示loading对话框，且不在意请求返回结果
     */
    @JvmStatic
    fun <T : BaseResponseData> request(call: Call<APIBaseResponse<T>>?) {
        request(call, null)
    }

    /*
     * 请求时默认不展示loading对话框
     */
    @JvmStatic
    fun <T : BaseResponseData> request(call: Call<APIBaseResponse<T>>?,
                                       callback: APIBase.ResponseListener<T>?) {
        request(null, call, callback)
    }

    @JvmStatic
    fun <T : BaseResponseData> request(context: Context?,
                                       call: Call<APIBaseResponse<T>>?,
                                       callback: APIBase.ResponseListener<T>?) {
        request(context, call, callback, null)
    }

    @JvmStatic
    fun <T : BaseResponseData> request(context: Context?,
                                       call: Call<APIBaseResponse<T>>?,
                                       callback: APIBase.ResponseListener<T>?,
                                       listener: UIUpdateListener?) {
        request(context, true, call, callback, listener)
    }

    @JvmStatic
    fun <T : BaseResponseData> request(context: Context?,
                                       showToastWhenFailed: Boolean,
                                       call: Call<APIBaseResponse<T>>?,
                                       callback: APIBase.ResponseListener<T>?,
                                       listener: UIUpdateListener?) {
        if (call == null) {
            return
        }
        if (SimpleUtil.hasNetwork(InjectUtil.context()!!)) {
            val retrofitCallback = RetrofitCallback<T>(callback)
            val req: TransformRequest<T> = TransformRequest<T>(call.request().url().toString(), callback)
            if (context != null && context is Activity) {
                val cancelListener: LoadingCancelListener = object : LoadingCancelListener {
                    override fun cancel() {
                        call?.cancel()
                    }
                }
                val controller = UIController(context, true, true, showToastWhenFailed, null)
                controller.listener = listener
                controller.cancelListener = cancelListener
                retrofitCallback.setUIController(controller)
                controller.start()
                req.controller = controller
            }
            listener?.hideRefreshView()

//            call.enqueue(retrofitCallback);
            req.uiListener = listener
            req.requestWithoutHeader(call.request().body(), callback)
        } else {
            if (context != null && showToastWhenFailed) {
                ToastUtil.show(context, R.string.no_network)
            }
            if (listener != null) {
                listener.showConnectExceptionView(false)
                listener.releaseToRefresh()
            }
        }
    }

    @JvmStatic
    fun <T : BaseResponseData> execute(context: Context?,
                                       call: Call<APIBaseResponse<T>?>?,
                                       listener: UIUpdateListener?,
                                       callback: APIBase.ResponseListener<T>?,
                                       showToastWhenFailed: Boolean) {
        if (call == null) {
            return
        }
        if (SimpleUtil.hasNetwork(InjectUtil.context()!!)) {
            val retrofitCallback = RetrofitCallback<T>(callback)
            if (context != null && context is Activity) {
                val cancelListener: LoadingCancelListener = object : LoadingCancelListener {
                    override fun cancel() {
                        call?.cancel()
                    }
                }
                val controller = UIController(context, true, true, showToastWhenFailed,
                        context.getResources().getString(R.string.loading))
                controller.listener = listener
                controller.cancelListener = cancelListener
                retrofitCallback.setUIController(controller)
                controller.start()
            }
            listener?.hideRefreshView()
            try {
                val rsp = call.execute()
                if (rsp.isSuccessful) {
                    Log.i("RetrofitCallback", "isSuccessful")
                }
                Log.i("RetrofitCallback", "execute")
            } catch (r: Throwable) {
                r.printStackTrace()
            }
        } else {
            if (context != null && showToastWhenFailed) {
                ToastUtil.show(context, R.string.no_network)
            }
            if (listener != null) {
                listener.showConnectExceptionView(false)
                listener.releaseToRefresh()
            }
        }
    }
}