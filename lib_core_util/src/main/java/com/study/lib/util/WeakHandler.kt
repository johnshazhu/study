package com.study.lib.util

import android.content.Context
import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

/**
 * Created by john on 2016/11/21.
 */
class WeakHandler(context: Context?, listener: MessageListener?) : Handler() {
    private val mContextReference: WeakReference<Context?>?
    private var mListener: MessageListener?
    private var mHaveContext = true
    fun setListener(listener: MessageListener?) {
        mListener = listener
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (mContextReference != null) {
            val context = mContextReference.get()
            if ((context != null || !mHaveContext) && mListener != null) {
                mListener!!.handleMessages(msg)
            }
        }
    }

    interface MessageListener {
        fun handleMessages(msg: Message?)
    }

    init {
        mHaveContext = context != null
        mContextReference = WeakReference(context)
        mListener = listener
    }
}