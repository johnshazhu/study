package com.drcuiyutao.lib.gson

import com.google.gson.Gson

object GsonUtil {
    fun <T> getString(data: T?): String {
        return if (data == null) "" else Gson().toJson(data)
    }
}
