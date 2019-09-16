package com.drcuiyutao.lib.api.base

import android.text.TextUtils
import com.drcuiyutao.lib.api.Constants

class APIBaseResponse<T: BaseResponseData> {
    val code: Int = Constants.CODE_ERROR
    var data: T? = null
    val msg: String? = null
    val routers: List<String>? = null
    private val bscode: String? = null

    val isSuccess get() = (code == Constants.CODE_OK) && (Constants.BIZ_CODE_OK == bscode || TextUtils.isEmpty(bscode))

    val isBusinessError get() = (code == Constants.CODE_OK) && (!TextUtils.isEmpty(bscode) && Constants.BIZ_CODE_OK != bscode) && !TextUtils.isEmpty(msg)

    val isEmpty get() = data?.isEmpty ?: false

    val yd get() = data?.yd ?: 0

    val showYuanDouText get() = data?.showText ?: ""

    val isWxNoUnionID get() = Constants.WX_NO_UNION_ID == code

    val isServerStopped get() = Constants.SERVER_UNAVAILABLE == code

    fun getBscode() = if (TextUtils.isEmpty(bscode)) code.toString() else bscode
}