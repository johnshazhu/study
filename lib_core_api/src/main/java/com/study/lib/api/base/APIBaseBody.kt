package com.study.lib.api.base

/**
 * Created by john on 2018/10/17.
 */
open class APIBaseBody : BaseBody {
    private var currentDate: Long

    constructor() {
        currentDate = System.currentTimeMillis()
    }

    constructor(currentDate: Long) {
        this.currentDate = currentDate
    }

    constructor(memberId: String?, currentDate: Long) {
        this.currentDate = currentDate
    }

    fun setCurrentDate(currentDate: Long) {
        this.currentDate = currentDate
    }
}