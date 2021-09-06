package com.study.lib.api

import java.net.URLEncoder
import java.util.*

/**
 * Created by zq on 2014/8/11.
 */
class ComparatorParam : Comparator<Any?> {
    override fun compare(arg0: Any?, arg1: Any?): Int {
        return if (arg0 is NameValuePair) {
            val param1 = arg1 as NameValuePair?
            arg0.name.compareTo(param1!!.name)
        } else {
            val param0 = arg0 as Header?
            val param1 = arg1 as Header?
            param0!!.name.compareTo(param1!!.name)
        }
    }

    companion object {
        @JvmStatic
        fun getEncodeString(value: String?): String? {
            try {
                return URLEncoder.encode(value, "UTF-8")
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }
    }
}