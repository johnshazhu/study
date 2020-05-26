package com.study.lib.api.base

import android.util.Log

class APIBaseBody: BaseBody {
    var currentDate: Long = 0

    constructor() {
        currentDate = System.currentTimeMillis()
    }

    constructor(time: Long) {
        currentDate = time
    }

    constructor(memberId: String, time: Long) {
        currentDate = time
        Log.i(this::class.java.simpleName, "memberId : $memberId")
    }
}