package com.study.lib.util

import android.os.Handler
import android.util.Log

/**
 * Created by gzq on 15/12/7.
 */
class LockTimer(overtime: Long, callback: LockTimerCallback?) {
    private var mOverTime: Long = 0
    var isLocked = false
        private set
    private val mHandler = Handler()
    private var mCallback: LockTimerCallback? = null
    private val mRunnable: Runnable = Runnable {
        Log.i(TAG, "mRunnable run mIsLocked[$isLocked]")
        isLocked = false
        mCallback?.overtime()
    }

    fun start() {
        Log.i(TAG, "start mIsLocked[$isLocked]")
        try {
            if (!isLocked) {
                mHandler.postDelayed(mRunnable, mOverTime)
                isLocked = true
            }
        } catch (e: Throwable) {
            Log.e(TAG, "start e[$e]")
        }
    }

    fun stop() {
        Log.i(TAG, "stop mIsLocked[$isLocked]")
        isLocked = false
        try {
            mHandler.removeCallbacks(mRunnable)
        } catch (e: Throwable) {
            Log.e(TAG, "stop e[$e]")
        }
    }

    interface LockTimerCallback {
        fun overtime()
    }

    companion object {
        private val TAG = LockTimer::class.java.simpleName
    }

    init {
        mOverTime = overtime
        mCallback = callback
    }
}