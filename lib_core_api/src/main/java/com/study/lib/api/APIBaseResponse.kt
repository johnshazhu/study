package com.study.lib.api

import android.text.TextUtils

open class APIBaseResponse<T : BaseResponseData> {
    private val bscode: String? = null
    var code = 0
    var msg: String? = null
    var data: T? = null
    val routers: List<String>? = null

    constructor() {}
    constructor(msg: String?, code: Int) {
        this.msg = msg
        this.code = code
    }

    val isSuccess: Boolean
        get() = code == 1 && ("1" == bscode || TextUtils.isEmpty(bscode))

    val isBusinessError = code == 1 && !TextUtils.isEmpty(bscode) && "1" != bscode && !TextUtils.isEmpty(msg)

    val isWxNoUnionID = ServerCode.WX_NO_UNION_ID == code

    val isServerStopped = ServerCode.SERVER_UNAVAILABLE == code

    fun getBscode() = if (TextUtils.isEmpty(bscode)) code.toString() else bscode!!

    val isEmpty = data?.isEmpty ?: false

    val yD = data?.yD ?: 0

    val showYuanDouText = data?.showText ?: ""
}