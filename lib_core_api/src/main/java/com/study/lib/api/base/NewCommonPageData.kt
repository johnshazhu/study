package com.study.lib.api.base

import com.study.lib.api.BaseResponseData

/**
 * Created by john on 2018/10/22.
 */
open class NewCommonPageData : BaseResponseData() {
    private val hasNext = false
    open val total = 0
    operator fun hasNext(): Boolean {
        return hasNext
    }
}