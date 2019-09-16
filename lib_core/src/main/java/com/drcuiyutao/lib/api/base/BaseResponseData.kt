package com.drcuiyutao.lib.api.base

import java.io.Serializable

open class BaseResponseData : EmptyListener, Serializable {
    val yd: Int = 0
    val showText: String? = null

    override val isEmpty get() = false
}