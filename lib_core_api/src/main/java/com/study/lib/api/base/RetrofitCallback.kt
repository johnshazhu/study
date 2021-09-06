package com.study.lib.api.base

import android.text.TextUtils
import android.util.Log
import com.study.lib.api.APIBase
import com.study.lib.api.APIBaseResponse
import com.study.lib.api.BaseResponseData
import com.study.lib.ui.UIController
import com.study.lib.util.InjectUtil
import com.study.lib.log.LogUtil.debug
import com.study.lib.R
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType

/**
 * Created by john on 2018/10/16.
 */
class RetrofitCallback<T : BaseResponseData>(private val mCallback: APIBase.ResponseListener<T>?) : Callback<APIBaseResponse<T>?> {
    private var mUIController: UIController? = null
    fun setUIController(controller: UIController?) {
        mUIController = controller
    }

    override fun onResponse(call: Call<APIBaseResponse<T>?>, response: Response<APIBaseResponse<T>?>) {
        Log.i("RetrofitCallback", "onResponse : " + Gson().toJson(response.body()))
        if (mUIController?.isCanceled == true) {
            mUIController!!.stop(true)
            mCallback?.onFailure(ResultCode.API_CANCEL, null)
            return
        }
        try {
            val rsp = response.body()
            if (rsp != null) {
                Log.i("RetrofitCallback", "rsp != null")
                mUIController?.let {
                    if (!rsp.isSuccess) {
                        mUIController!!.toastMsg = rsp.msg
                    }
                    mUIController!!.stop(true)
                }
                InjectUtil.parseRouters(rsp.routers)
                if (mCallback != null) {
                    if (rsp.isSuccess) {
                        if (rsp.data == null) {
                            val types = mCallback.javaClass.genericInterfaces
                            if (types.isNotEmpty()) {
                                for (type in types) {
                                    if (type !is ParameterizedType) {
                                        continue
                                    }
                                    val targetType = type.actualTypeArguments[0]
                                    val targetObj = Gson().fromJson<Any>("{}", targetType)
                                    if (targetObj is BaseResponseData) {
                                        rsp.data = targetObj as T
                                        break
                                    }
                                }
                            }
                        }
                        if (rsp.isEmpty) {
                            showEmptyView()
                        }
                        mCallback.onSuccess(rsp.data, Gson().toJson(response.body()), rsp.getBscode(), rsp.msg, true)
                    } else {
                        showConnectExceptionView()
                        mCallback.onFailure(response.code(), rsp.toString())
                    }
                }
            } else if (response.errorBody() != null) {
                var error = "default error"
                Log.i("RetrofitCallback", "errorBody != null")
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    error = errorBody.string()
                    if (!TextUtils.isEmpty(error)) {
                        debug(error)
                    }
                }
                if (!response.isSuccessful) {
                    stopLoading(false)
                    showConnectExceptionView()
                }
                mCallback?.onFailure(response.code(), error)
            } else {
                Log.i("RetrofitCallback", "onException")
                onException(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onException(null)
        }
    }

    override fun onFailure(call: Call<APIBaseResponse<T>?>, t: Throwable) {
        Log.i("RetrofitCallback", "onFailure")
        showConnectExceptionView()
        stopLoading(false)
        if (mCallback != null) {
            if (t is IOException && RetrofitBase.CANCELED == t.message) {
                mCallback.onFailure(ResultCode.API_CANCEL, null)
            } else {
                mCallback.onFailure(ResultCode.API_SERVER_ERROR, null)
            }
        }
    }

    private fun stopLoading(isDataException: Boolean) {
        mUIController?.setToastMsgId(if (isDataException) R.string.data_exception else R.string.network_exception)
        mUIController?.stop(true)
    }

    private fun onException(t: Throwable?) {
        stopLoading(t == null)
        mCallback?.onFailure(ResultCode.API_EXCEPTION, null)
    }

    private fun showConnectExceptionView() {
        mUIController?.listener?.showConnectExceptionView(true)
    }

    private fun showEmptyView() {
        mUIController?.listener?.showEmptyContentView()
    }
}