package com.study.lib.api.v66

import java.io.Serializable

/**
 * Created by john on 2018/1/19.
 */
class ComponentModel(var text: String) : Serializable {
    val componentType: String? = null
    val skipModel: SkipModel? = null
    val type = 0
    var radius = 0
    var checked = 0
    var name: String? = null

    fun isChecked() = 1 == checked

    fun setChecked(checked: Boolean) {
        this.checked = if (checked) 1 else 0
    }

    companion object {
        const val BTN_GREEN = 1
        const val BTN_RED = 2
        const val BTN_GREEN_SOLID = 3
        const val RADIUS_0 = 0
        const val RADIUS_16 = 1
        const val RADIUS_20 = 2
        const val RADIUS_50 = 3
    }
}