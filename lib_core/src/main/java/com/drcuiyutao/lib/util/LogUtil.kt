package com.drcuiyutao.lib.util

import android.util.Log

object LogUtil {
    private const val TAG = "xdebug"
    @Volatile
    var debug = true

    fun debug(msg: String) {
        if (debug) {
            Log.i(TAG, msg)
        }
    }
}
