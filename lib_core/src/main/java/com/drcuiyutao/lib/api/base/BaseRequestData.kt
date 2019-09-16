package com.drcuiyutao.lib.api.base

import com.drcuiyutao.lib.api.Constants
import com.drcuiyutao.lib.util.LogUtil

class BaseRequestData private constructor() {
    var userID: Int = 0
    private var token: String? = null
    private var t: String? = null
    private var sign: String? = null
    private var deviceno: String? = null
    private var birthday: String? = null
    private var clientversion: String? = null
    private val ostype: Int = Constants.DEVICE_TYPE
    private var osversion: String? = null
    private var appcode: String? = null
    private var channel: String? = null
    private var imei: String? = null
    private var android_id: String? = null
    private var build_serial: String? = null
    private var yxyskin: Int = 0
    private var hgestation: Int = 0
    private var packageName: String? = null
    private var expectedDate: String? = null
    private var prematureOpen: Int = 0
    private var accountId = ""
    private var loginType: Int = 0
    private var deviceuuid: String? = null

    @Transient
    private var isGuest = true
    @Transient
    var childId = ""
    @Transient
    var memberId2 = ""

    init {
        LogUtil.debug("BaseRequestData constructor init block : $this")
    }

    private object BaseRequestDataHolder {
        val instance = BaseRequestData()
    }

    companion object {
        val instance: BaseRequestData by lazy {
            BaseRequestData()
        }
//        val instance = BaseRequestDataHolder.instance
    }
}