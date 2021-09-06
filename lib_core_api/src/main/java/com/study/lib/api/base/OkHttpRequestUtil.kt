package com.study.lib.api.base

import android.os.Message
import com.study.lib.api.APIBaseRequest
import com.study.lib.api.BaseResponseData
import com.study.lib.api.base.ResponseHandlerUtil.parseFailure
import com.study.lib.api.base.ResponseHandlerUtil.parseResponse
import com.study.lib.util.WeakHandler
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Created by john on 2018/10/18.
 */
class OkHttpRequestUtil : WeakHandler.MessageListener {
    private var mHandler: WeakHandler? = null
    private var mCall: Call? = null
    private var mResponse: Response? = null
    private var mResult: String? = null
    private var mException: IOException? = null
    private var mRequest: APIBaseRequest<*>? = null
    private var isInThread = false

    constructor(isInThread: Boolean) {
        this.isInThread = isInThread
    }

    fun <T : BaseResponseData> request(call: Call, request: APIBaseRequest<T>?) {
        mRequest = request
        val callback: Callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mCall = call
                mException = e
                mResult = null
                if (mHandler != null) {
                    mHandler!!.sendEmptyMessage(ResultCode.RESULT_FAIL)
                } else {
                    try {
                        parseFailure(call, null, null, mRequest!!, mException)
                    } catch (r: Throwable) {
                        r.printStackTrace()
                    }
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                mCall = call
                mResponse = response
                mResult = response.body()!!.string()
                if (mHandler != null) {
                    mHandler!!.sendEmptyMessage(ResultCode.RESULT_OK)
                } else {
                    parseResponse(mCall!!, mResponse!!, mResult!!, mRequest!!)
                }
            }
        }
        if (!isInThread) {
            mHandler = WeakHandler(null, this)
        }
        call.enqueue(callback)
    }

    fun reset() {
        mHandler!!.removeCallbacksAndMessages(null)
        mHandler = null
    }

    override fun handleMessages(msg: Message?) {
        try {
            when (msg!!.what) {
                ResultCode.RESULT_OK -> parseResponse(mCall!!, mResponse!!, mResult!!, mRequest!!)
                ResultCode.RESULT_FAIL -> parseFailure(mCall!!, null, mResult, mRequest!!, mException)
                else -> {
                }
            }
        } catch (r: Throwable) {
            r.printStackTrace()
        }
        reset()
    }
}