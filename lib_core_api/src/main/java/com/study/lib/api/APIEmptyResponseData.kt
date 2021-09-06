package com.study.lib.api

import com.study.lib.api.v66.ComponentModel
import com.study.lib.util.SimpleUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by john on 2015/4/18.
 */
open class APIEmptyResponseData : BaseResponseData() {
    @SerializedName(value = "id")
    val strId: String? = null
    @SerializedName(value = "hints")
    val recordTips: List<RecordTip>? = null
    val isnewhint = 0 //0：无更新 1：有更新
    val coupId = 0
    val recipesId = 0
    @SerializedName(value = "shareurl")
    val shareUrl: String? = null
    val createTime: String? = null
    val count = 0

    val isNewHint: Boolean
        get() = isnewhint == 1

    fun getId(): Int {
        return SimpleUtil.parseLong(strId).toInt()
    }

    class RecordTip(val title: String) : Serializable {
        @SerializedName(value = "b_id")
        val bussinessId = 0
        val id = 0
        val content: String? = null
        @SerializedName(value = "know_title")
        val knowledgeTitles: String? = null
        @SerializedName(value = "know_ids")
        val knowledgeIds: String? = null
        val shareurl: String? = null
        val linkShow = 0 //是否显示跳转 1:显示   0：不显示 = 0
        val linkUrl: String? = null
        val type = 0
        private val b_day: Long = 0
        @SerializedName(value = "create_time")
        val dateInfo: String? = null
        private val isread = 0
        private val templet_id = 0
        private val d: String? = null
        val list: List<ComponentModel>? = null
        val remindList: List<RecordIntro>? = null

        val timestamp: Long
            get() = b_day * 1000
    }

    class RecordIntro : Serializable {
        val link: ComponentModel? = null
        val showText: String? = null
        val type = 0
    }
}