package com.study.doc.test

import android.util.Log
import android.view.View

class TestJava {
    private fun test() {
        val i = 1
        Log.i("xdebug", i.toString())
        val a = View(null)
        a.postDelayed({ Log.i("xdebug", "test") }, 1000)
    }

    companion object {
        private val COUT: Long = 3
        private val TAG = TestJava::class.java.simpleName

        fun insertKotlin() {

        }

        fun insertKotlinReplace() {
        }
    }
}
