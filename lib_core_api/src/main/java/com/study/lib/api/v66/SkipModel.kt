package com.study.lib.api.v66

import com.study.lib.api.OmittedAnnotation
import com.study.lib.json.JsonUtil.toJson
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by john on 2018/1/19.
 */
class SkipModel : Serializable {
    @SerializedName(value = "tourl")
    var toUrl: String? = null
    var type = 0
    val data: Data? = null
    var from: String? = null
    @SerializedName(value = "bind")
    var isBind = false
    private val needlogin: NeedLogin? = null
    val create: List<String>? = null
    val destroy: List<String>? = null
    var cytss: LinkedList<Map<String, Any>>? = null
    var yxys: String? = null

    override fun toString(): String {
        return "[type[$type] tourl[$toUrl] needlogin[$needlogin]]"
    }

    fun isNeedlogin(): Boolean {
        return null != needlogin
    }

    val needLoginJsonString: String?
        get() = needlogin?.needLoginJsonString

    class Data : Serializable {
        val type = 0
        val desc: String? = null

    }

    class ToUrlInfo : Serializable {
        val index = 0
        val subindex = 0
        val id = 0
        val tag: String? = null
        val title: String? = null
        val url: String? = null
        val type = 0
        val couponId: String? = null
        val couponGroupId: String? = null
        val packageId = 0
            get() = if (field == 0) -1 else field
        val buttonCase: String? = null
        val checkBox = 0
        val payType = 0
        val reply: ReplyInfo? = null
        val recordType = 0
        @SerializedName(value = "toRecordTab")
        val isToRecordTab = false
        @SerializedName(value = "toDetail")
        val isToDetail = false
        val height = 0
        val issupport = 0
        val goodsId = 0
        var orderCode: String? = null
        @SerializedName(value = "yuandou")
        val yuanDou: String? = null
        val bizType = 0
        var from: String? = null
        val sn: String? = null
        @SerializedName(value = "jump")
        val skipModel: SkipModel? = null
        val userName: String? = null
        val path: String? = null
        val miniProgramType = 0
        val scene = 0
        val templateID: String? = null
        val reserved: String? = null
        val bizArgs: String? = null
    }

    class ReplyInfo : Serializable {
        val commentId = 0
        val commentContent: String? = null
        private val uId = 0
        private val uNickName: String? = null

        fun getuId(): Int {
            return uId
        }

        fun getuNickName(): String? {
            return uNickName
        }
    }

    class NeedLogin : Serializable {
        var type = 0
        var alert: String? = null

        constructor() {}
        constructor(type: Int, alert: String?) {
            this.type = type
            this.alert = alert
        }

        val isLogin: Boolean
            get() = 0 == type

        val needLoginJsonString: String
            get() = toJson(this)

        override fun toString(): String {
            return "[NeedLogin type[$type] alert[$alert]]"
        }

        companion object {
            @JvmField
            @OmittedAnnotation
            val NEED_LOGIN_TYPE_LOGIN = 0

            @JvmField
            @OmittedAnnotation
            val NEED_LOGIN_TYPE_REGISTER = 1
        }
    }

    companion object {
        const val TYPE_ORIGINAL_BTN = 10
        const val TYPE_ORIGINAL_PUSH = 2
        const val TYPE_PUSH_URL = 3
        const val TYPE_TO_BE_VIP_PUSH = 4
        const val TYPE_TO_YOU_ZAN = 9
        const val TYPE_TO_VIP_ZONE = 100
        const val TYPE_TO_MAIN_TAB = 101
        const val TYPE_TO_AUDIO = 102
        const val TYPE_TO_LECTURE = 103
        const val TYPE_TO_VIP_CENTER = 104
        const val TYPE_TO_COUPON = 105
        const val TYPE_RIGHT_BTN = 106
        const val TYPE_TO_VIP_RECORD = 107
        const val TYPE_VIP_CODE_ACTIVE = 108
        const val TYPE_GROWING_IO = 109
        const val TYPE_GET_PAY_INFO = 110
        const val TYPE_TO_COURSE = 111
        const val TYPE_TO_ADD_RECORD = 112
        const val TYPE_PAY_TYPE = 113
        const val TYPE_PROTOCOL_CHECK = 114
        const val TYPE_TO_PAY = 115
        const val TYPE_USER_STATUS_CHANGE = 116
        const val TYPE_OPEN_BROWSER = 118
        const val TYPE_MY_EXCHANGE = 120
        const val TYPE_H5_HEIGHT_CHANGE = 121
        const val TYPE_SHARE_IMAGE = 122
        const val TYPE_GOTO_SHARE = 123
        const val TYPE_VIRTUAL_MONEY_DETAIL = 124
        const val TYPE_FEEDBACK = 125
        const val TYPE_WEBVIEW_BANNER_SUPPORT = 126
        const val TYPE_DISCUSS_LIST = 127
        const val TYPE_SPECIAL_LIST = 128
        const val TYPE_TALENTS_LIST = 129
        const val TYPE_AUDIO_ALBUM = 130
        const val TYPE_PERSONAL_INFO_CENTER = 131
        const val TYPE_JUMP_TO_PAY = 132
        const val TYPE_BIND_RESULT = 133
        const val TYPE_ROUTER_PATH = 134
        const val TYPE_AD_CLICK = 135
        const val TYPE_SCHEME_JUMP = 136
        const val TYPE_COMMON_PAY = 137
        const val TYPE_WX_MINI_PROGRAM = 138
        const val TYPE_WX_SUBSCRIBE_MESSAGE = 139
        const val TYPE_QRCODE_SCAN_RESULT = 140
        const val TYPE_CLEAR_CACHE = 141
        const val TYPE_SET_IS_WEBVIEW_CACHE = 142
        const val TYPE_SHARE = 143
        const val TYPE_SHARE_SUCCESS = 144
        const val TYPE_CHECK_VERSION = 145
        const val TYPE_CHECK_SKIN = 146
        const val TYPE_CHECK_CHEERFUL = 147
        const val TYPE_RIGHT_BTN_IMAGE = 148
        const val TYPE_TOKEN = 149
        const val TYPE_H5_API_FAILED = 150
        const val TYPE_H5_COUPON_REFRESH = 151
        const val TYPE_APP_H5_PAGE_RESUME = 152
        const val TYPE_APP_H5_PAGE_PAUSE = 153
        const val TYPE_H5_APP_PAGE_FINISH = 154
        const val TYPE_H5_APP_MEDIA_PLAY = 155
        const val TYPE_APP_H5_MEDIA_PLAY = 156
        const val TYPE_CYTSS_H5_APP = 157 // web页获取来源列表
        const val TYPE_CYTSS_APP_H5 = 158 // app返回来源列表
        const val TYPE_CYTSS_H5_UPDATE = 159 // web页更新原生来源列表
        const val TYPE_H5_APP_WEBVIEW_GOBACK = 160
        const val TYPE_H5_APP_ACTIONBAR_SHOW_HIDE = 161
        const val TYPE_GIO_SET_EVAR = 162
        const val TYPE_GIO_SET_VARIABLE = 164
        const val TYPE_WEBVIEW_GET_ZOOM = 165
        const val TYPE_WEBVIEW_SET_ZOOM = 166
        const val TYPE_WEBVIEW_GET_SCALE = 167
        const val TYPE_WEBVIEW_SET_INITIAL_SCALE = 168
        const val TYPE_WEBVIEW_SET_SCALE_X = 169
        const val TYPE_WEBVIEW_SET_SCALE_Y = 170
        const val TYPE_YXYS_H5_APP = 171 // web页获取来源列表
        const val TYPE_YXYS_APP_H5 = 172 // app返回来源列表
        const val TYPE_YXYS_H5_UPDATE = 173 // web页更新原生来源列表
        const val TYPE_YXYS_H5_SIGN_UPDATE = 174
        const val TYPE_YXYS_H5_SIGN_REFRESH_TASK = 175
        const val TYPE_YXYS_H5_CHECK_PUSH_STATUS = 176
        const val TYPE_YXYS_H5_REFRESH_PUSH_STATUS = 177
        const val TYPE_YXYS_H5_OPEN_PUSH_STATUS = 178
        const val SKIP_URL_EXCHANGE = "10001"
        const val SKIP_URL_KNOW = "10002"
        const val SKIP_URL_EXCHANGE_CONFIRM = "10003"
        const val SKIP_URL_BROWSE = "10004"
        const val SKIP_URL_EARN = "10005"
        const val SKIP_URL_SIGN = "11001"
        const val SKIP_URL_BIND = "12001"
        const val SKIP_URL_EVA = "13001"
        const val SKIP_URL_RECORD = "14001"
        const val SKIP_URL_RECORD_PREGNANT = "14002"
    }
}