package com.study.doc.test

import com.study.lib.api.base.BaseResponseData

class StartUpData: BaseResponseData() {
    val appConfigSwitchList: List<ConfigSwitch>? = null
    val mallTab: TabInfo? = null
    val vipTab: TabInfo? = null
    val list: List<SkipModel>? = null

    override val isEmpty get() = (appConfigSwitchList?.size ?: 0) == 0
}