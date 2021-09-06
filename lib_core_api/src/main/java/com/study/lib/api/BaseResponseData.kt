package com.study.lib.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseResponseData : Serializable {
    @SerializedName(value = "yd")
    val yD = 0
    val showText: String? = null

    /**
     * @return 数据是否是空, 用于ui中展示无数据页面控制, 可在子类中覆盖该方法
     */
    open val isEmpty: Boolean
        get() = false
}