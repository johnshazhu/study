package com.study.lib.util

import android.content.Context

object InjectUtil {
    fun setStatsSwitch(switchMask: Int) {

    }

    fun getStatsSwitch(): Int {
        return 0
    }

    fun getUserStatus(context: Context): Int {
        return 0
    }

    fun parseRouters(routers: List<String>?): Boolean {
        return false
    }

    fun context(): Context? {
        return null
    }

    fun city(): String? {
        return null
    }

    fun province(): String? {
        return null
    }

    fun traceId(): String {
        return ""
    }

    fun forceLogOut(msg: String?, isNoUnionId: Boolean) {

    }
}