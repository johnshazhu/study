package com.study.lib.api

import android.net.Uri
import com.study.lib.log.LogUtil
import com.study.lib.util.SimpleUtil
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Created by john on 2017/12/22.
 */
class APISchemeAuthorityConfig private constructor() {
    private val mSchemeAuthorityMap: Map<String, String>
    private val mSchemeAuthoritySwitchMap: ConcurrentMap<String, Boolean>
    fun getNewSchemeAuthority(schemeAuthority: String): String? {
        return mSchemeAuthorityMap[schemeAuthority]
    }

    fun setSchemeAuthoritySwitchFlag(schemeAuthority: String?) {
        if (schemeAuthority in mSchemeAuthoritySwitchMap) {
            mSchemeAuthoritySwitchMap[schemeAuthority] = true
            mSchemeAuthoritySwitchMap[mSchemeAuthorityMap[schemeAuthority]] = false
        }
    }

    fun isNeedSwitch(schemeAuthority: String?): Boolean {
        return (schemeAuthority in mSchemeAuthoritySwitchMap) &&
                mSchemeAuthoritySwitchMap[schemeAuthority]!!
    }

    fun resetSchemeAuthoritySwitchFlagMap() {
        if (mSchemeAuthoritySwitchMap.isNotEmpty()) {
            for (key in mSchemeAuthoritySwitchMap.keys) {
                mSchemeAuthoritySwitchMap[key] = false
            }
        }
    }

    private fun initSchemeAuthorityMap(): Map<String, String> {
        val map: Map<String, String> = mapOf(
                APIConfig1.BASE to BASE,
                APIConfig1.PUSH_BASE to PUSH_BASE,
                APIConfig1.NEW_API_BASE_HOST to NEW_API_BASE_HOST2,
                APIConfig1.NETWORK_COLLECT_BASE to "",
                BASE to APIConfig1.BASE,
                PUSH_BASE to APIConfig1.PUSH_BASE,
                NEW_API_BASE_HOST2 to APIConfig1.NEW_API_BASE_HOST
        )
        LogUtil.debug("initSchemeAuthorityMap : $map")
        return map
    }

    private fun initSchemeAuthoritySwitchMap(): ConcurrentHashMap<String, Boolean> {
        val map: ConcurrentHashMap<String, Boolean> = ConcurrentHashMap()
        map[APIConfig1.BASE] = false
        map[APIConfig1.PUSH_BASE] = false
        map[APIConfig1.NEW_API_BASE_HOST] = false
        map[APIConfig1.NETWORK_COLLECT_BASE] = false
        map[BASE] = false
        map[PUSH_BASE] = false
        map[NEW_API_BASE_HOST2] = false
        /*mapOf(
                APIConfig1.BASE to false,
                APIConfig1.PUSH_BASE to false,
                APIConfig1.NEW_API_BASE_HOST to false,
                APIConfig1.NETWORK_COLLECT_BASE to false,
                BASE to false,
                PUSH_BASE to false,
                NEW_API_BASE_HOST2 to false
        ) as ConcurrentHashMap<String, Boolean>*/
        LogUtil.debug("initSchemeAuthoritySwitchMap : $map")
        return map
    }

    val hostList: ArrayList<String>?
        get() {
            if (mSchemeAuthorityMap.isNotEmpty()) {
                val map: MutableMap<String, Boolean> = HashMap()
                val hostList = ArrayList<String>()
                for (url in mSchemeAuthorityMap.keys) {
                    Uri.parse(url).host?.let { host ->
                        if (!map.containsKey(host)) {
                            map[host] = true
                            LogUtil.debug("addHost : $host")
                            hostList.add(host)
                        }
                    }
                }
                return hostList
            }
            return null
        }

    object APIHostConfigHolder {
        @JvmStatic
        var sInstance = APISchemeAuthorityConfig()
    }

    companion object {
        var sSwitchAllApiSchemeAuthority = true
        const val BASE = "https://api3.drcuiyutao.com"
        const val PUSH_BASE = "https://tk3.drcuiyutao.com"
        val NEW_API_BASE_HOST2 = SimpleUtil.getPropertyValue("new_api_base_host2")
        @JvmStatic
        val instance: APISchemeAuthorityConfig
            get() = APIHostConfigHolder.sInstance
    }

    init {
        mSchemeAuthorityMap = initSchemeAuthorityMap()
        mSchemeAuthoritySwitchMap = initSchemeAuthoritySwitchMap()
        LogUtil.debug("APIConfig1.BASE : " + APIConfig1.BASE + ", online : " + sSwitchAllApiSchemeAuthority)
    }
}