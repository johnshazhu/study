package com.study.lib.api

import android.text.TextUtils
import com.study.lib.util.InjectUtil
import com.study.lib.log.LogUtil.e
import com.study.lib.log.LogUtil.i
import java.util.*

object APIBase {
    private val TAG = APIBase::class.java.simpleName
    private fun checkParam(param: String?): Boolean {
//        String[] checklist = {"userID", "t", "deviceno", "clientversion", "ostype", "osversion", "channel", "appcode"};
        return param in arrayOf(APIUtils.TIME, APIUtils.DEVICE_NO)
    }

    @JvmStatic
    fun getSignHeader(url: String?, headers: Array<Header>?, params: List<NameValuePair>?): Array<Header>? {
        var result = headers
        try {
            val headerList = ArrayList<Header>()
            headers?.let {
                var signIndex = -1
                for (i in headers.indices) {
                    if (APIUtils.SIGN == headers[i].name) {
                        signIndex = i
                    } else if (checkParam(headers[i].name)) {
                        headerList.add(headers[i])
                    }
                }
                val sign: String = APIUtils.nativeGetParam(InjectUtil.context(), headerList, params)
                i(TAG, "post sign[$sign]")
                if (!TextUtils.isEmpty(sign)) {
                    BaseRequestData.instance.sign = sign
                    if (signIndex == -1) {
                        result = headers.plus(Header(APIUtils.SIGN, sign))
                    } else {
                        headers[signIndex].value = sign
                        return headers
                    }
                }
            }
        } catch (e: Throwable) {
            e(TAG, "post e[$e]")
            e.printStackTrace()
        }
        return result
    }

    interface ResponseListener<T> {
        fun onSuccess(data: T?, result: String?, code: String?, msg: String?, isBusinessSuccess: Boolean)
        fun onFailure(code: Int, result: String?)
        @JvmDefault fun onFailureWithException(url: String?, e: Exception?) {}
    }
}