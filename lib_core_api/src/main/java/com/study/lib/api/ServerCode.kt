package com.study.lib.api

object ServerCode {
    const val TOKEN_EXPIRED = "-10"

    //仅针对微信授权登录用，旧版登录使用openId，对此类用户在接口访问时踢出登录让其重新授权登录
    const val WX_NO_UNION_ID = 406
    const val SERVER_UNAVAILABLE = 503
}