package com.drcuiyutao.lib.api.base

interface ResponseListener<T> {
    fun onBizSuccess(response: T?) {}

    fun onBizFailure(response: T?) {}

    fun onFailure(code: Int, result: String?) {}

    fun onFailureWithException(url: String?, e: Exception?) {}
}