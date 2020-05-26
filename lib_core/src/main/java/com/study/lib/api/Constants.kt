package com.study.lib.api

object Constants {
    const val CODE_OK = 1
    const val CODE_ERROR = 0
    const val BIZ_CODE_OK = "1"

    //仅针对微信授权登录用，旧版登录使用openId，对此类用户在接口访问时踢出登录让其重新授权登录
    const val WX_NO_UNION_ID = 406

    const val SERVER_UNAVAILABLE = 503

    const val DEVICE_TYPE = 2
}