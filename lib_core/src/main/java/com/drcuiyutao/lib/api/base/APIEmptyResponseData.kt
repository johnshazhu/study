package com.drcuiyutao.lib.api.base

import com.drcuiyutao.lib.model.RecordTip

class APIEmptyResponseData: BaseResponseData() {
    val id: String? = ""
    val count: Int = 0
    val coupId: String? = ""
    val recipesId: String? = ""
    val shareurl: String? = ""
    val createTime: String? = ""
    val isnewhint: Int = 0
    val hints: List<RecordTip>? = null
}