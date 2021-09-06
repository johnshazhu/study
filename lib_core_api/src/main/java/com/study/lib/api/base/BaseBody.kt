package com.study.lib.api.base

import com.study.lib.api.BaseRequestData

/**
 * Created by john on 2018/10/26.
 */
open class BaseBody {
    val appDevice: BaseRequestData

    init {
        appDevice = BaseRequestData.instance
    }
}