package com.study.lib.sys

import android.app.Application
import android.content.Context

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        private var context: Context? = null

        fun getContext(): Context? {
            return context
        }
    }
}
