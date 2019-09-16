package com.drcuiyutao.lib.api.base

open class BaseBody {
    val childId get() = appDevice?.childId

    val memberId2 get() = appDevice?.memberId2

    var cts: Long = -1

    private val appDevice: BaseRequestData? = null

    val uid get() = appDevice?.userID

    fun getAppDevice(): BaseRequestData {
        return appDevice ?: BaseRequestData.instance
    }
}
