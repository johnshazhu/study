package com.study.lib.util

import com.study.lib.api.base.RetrofitBase.Companion.client
import okhttp3.Dns

object HttpDnsUtil {
    @Volatile
    @JvmStatic
    var isDnsSwitchOn = true //默认httpDns开启

    @JvmStatic
    fun getResolvedIp(host: String?): String? {
        return null
    }

    @JvmStatic
    val dns: Dns?
        get() = null

    @JvmStatic
    val isUseLocalDns: Boolean
        get() = true

    @JvmStatic
    fun init(isMain: Boolean, isOn: Boolean) {
        if (isMain) {
            isDnsSwitchOn = isOn
            client
        }
    }
}