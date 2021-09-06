package com.study.lib.api

import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.google.gson.annotations.SerializedName
import com.study.lib.util.InjectUtil
import com.study.lib.util.SimpleUtil
import com.study.lib.util.UUIDUtil
import com.study.lib.util.UserUtil

class BaseRequestData private constructor() {
    var userID = 0
    var token: String? = null
    var t: String? = null
    var sign: String? = null
    var deviceno: String? = null
    var birthday: String? = null
    var clientversion: String? = null
    var ostype = 0
    var osversion: String? = null
    var appcode: String? = null
    var channel: String? = null
    var imei: String? = null
    @SerializedName("android_id")
    var androidId: String? = null
    @SerializedName("build_serial")
    var buildSerial: String? = null
    var yxyskin = 0
    var hgestation = 0
    var packageName: String? = null
    var expectedDate: String? = null
    var prematureOpen = 0
    var accountId: String? = null
    var loginType = 0
    var deviceuuid: String? = null
    var abType = 0

    @Transient
    var isGuest = true

    @Transient
    var childId: String? = ""

    @Transient
    var memberId2: String? = ""

    init {
        Log.i("BaseRequestData", "BaseRequestData constructor init block : $this")
        val context = InjectUtil.context()!!
        val mac = SimpleUtil.getMacAddress()
        val uuid = UUIDUtil.getUUID()
        deviceno = if (TextUtils.isEmpty(mac)) uuid else mac
        ostype = 2
        osversion = Build.VERSION.RELEASE
        appcode = SimpleUtil.getPropertyValue(APIConfig1.CONFIG_APP_CODE)
        channel = APIUtils.appChannel
        clientversion = APIUtils.appVersion
        imei = SimpleUtil.getImei(context)
        androidId = SimpleUtil.getAndroidId(context)
        buildSerial = SimpleUtil.getBuildSerial(context)
        packageName = context.packageName
        deviceuuid = uuid
        setUserInfo(UserUtil.userId(), UserUtil.token(),
                UserUtil.babyBirthday(), UserUtil.expectedDate(), UserUtil.prematureOpen())
        loginType = UserUtil.getLoginType()
        hgestation = UserUtil.getIsGestation()
        yxyskin = UserUtil.getYxySkin()
        childId = UserUtil.getCurChildId()
        memberId2 = UserUtil.getMemberId2()
    }

    fun setUserInfo(uid: Int, token: String?, birthday: String?, expectedDate: String?, prematureOpen: Boolean) {
        userID = uid
        this.token = token
        this.birthday = birthday
        this.expectedDate = expectedDate
        this.prematureOpen = if (prematureOpen) 1 else 0
    }

    fun logout() {
        setUserInfo(0, "", null, null, false)
        hgestation = -1
        loginType = -1
        isGuest = false
        accountId = ""
        childId = ""
        memberId2 = ""
    }

    companion object {
        @JvmStatic
        val instance: BaseRequestData by lazy {
            BaseRequestData()
        }
    }
}