package com.study.lib.log

import android.content.Context
import android.util.Log
import com.study.lib.log.Logcat2File.Companion.instance
import com.study.lib.util.SimpleUtil

object LogUtil {
    var mDebug = true

    @JvmStatic
    fun isDebug() = mDebug

    /**
     * 开关log
     *
     * @param context ：true开， false关
     */
    @JvmStatic
    fun init(context: Context?) {
        val debug: String? = SimpleUtil.getPropertyValue("debug")
        if ("none".equals(debug, ignoreCase = true)) {
            mDebug = false
        } else {
            mDebug = true
            if ("file".equals(debug, ignoreCase = true)) {
                instance!!.start(context!!)
            }
        }
    }

    @JvmStatic
    fun teminate(context: Context?) {
        val debug: String? = SimpleUtil.getPropertyValue("debug")
        if ("file".equals(debug, ignoreCase = true)) {
            instance!!.stop()
        }
    }

    @JvmStatic
    fun d(tag: String?, str: String?) {
        if (mDebug) {
            try {
                Log.d(tag, str)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun w(tag: String?, str: String?) {
        if (mDebug) {
            try {
                Log.w(tag, str)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun e(tag: String?, str: String?) {
        if (mDebug) {
            try {
                Log.e(tag, str)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun i(tag: String?, str: String?) {
        if (mDebug) {
            try {
                Log.i(tag, str)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun v(tag: String?, str: String?) {
        if (mDebug) {
            try {
                Log.v(tag, str)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun d(str: String?) {
        v("LogUtil", str)
    }

    @JvmStatic
    fun debug(infor: String?) {
        i("xdebug", infor)
    }

    @JvmStatic
    fun debugWithFile(infor: String?) {
        try {
            debug(infor)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}