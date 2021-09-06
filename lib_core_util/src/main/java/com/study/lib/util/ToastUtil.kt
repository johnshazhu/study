package com.study.lib.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.study.lib.common.R
import java.lang.reflect.Field

object ToastUtil {
    private val TAG = ToastUtil::class.java.simpleName
    const val TOAST_DURATION = Toast.LENGTH_SHORT
    private var sLastToastTimestamp: Long = 0
    private var mToast: Toast? = null

    @JvmStatic fun show(msg: String?) {
        show(InjectUtil.context()!!, msg)
    }

    /**
     * 显示Toast
     *
     * @param context : 上下文
     * @param msgId   : 提示信息id
     */
    @JvmStatic fun show(context: Context, msgId: Int) {
        if (msgId == R.string.no_network) {
            val cur = System.currentTimeMillis()
            Log.i(TAG, "diff : " + (cur - sLastToastTimestamp))
            if (sLastToastTimestamp == 0L || cur - sLastToastTimestamp > 200 || cur < sLastToastTimestamp) {
                sLastToastTimestamp = cur
                show(context, msgId, TOAST_DURATION)
            }
        } else {
            show(context, msgId, TOAST_DURATION)
        }
    }

    /**
     * 显示Toast
     *
     * @param msg      : 提示信息
     * @param duration ：显示时长
     */
    @SuppressLint("ShowToast")
    @Synchronized
    @JvmStatic fun show(msg: String?, duration: Int) {
        try {
            if (mToast == null) {
                mToast = Toast.makeText(InjectUtil.context()!!, msg, duration)
                Log.i(TAG, "new Toast")
            } else {
                mToast!!.cancel()
                mToast!!.setText(msg)
                mToast!!.duration = duration
                Log.i(TAG, "set Toast")
            }
            hookToast(mToast)
            mToast!!.show()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 显示Toast
     *
     * @param context : 上下文
     * @param msg     ：提示信息
     */
    @JvmOverloads
    @JvmStatic fun show(context: Context?, msg: String?, duration: Int = TOAST_DURATION) {
        try {
            val toast = Toast.makeText(context, msg, duration)
            hookToast(toast)
            toast.show()
            //            Toast.makeText(context, msg, duration).show();
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 显示Toast
     *
     * @param context  : 上下文
     * @param msgId    : 提示信息id
     * @param duration ：显示时长
     */
    @JvmStatic fun show(context: Context, msgId: Int, duration: Int) {
        try {
            show(context, context.getString(msgId), duration)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @JvmStatic fun showTopToast(context: Context?, msg: String?) {
        if (context != null) {
            val toast = Toast(context)
            val v: View = LayoutInflater.from(context).inflate(R.layout.toast_view, null, false)
            val tv = v.findViewById<View>(R.id.toast_tips) as TextView
            tv.text = msg
            toast.setMargin(0f, 0f)
            toast.view = v
            toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 0)
            toast.duration = Toast.LENGTH_SHORT
            hookToast(toast)
            toast.show()
        }
    }

    @JvmStatic fun showBeanGetToast(context: Context?, msg: String?, count: String) {
        var count = count
        if (context != null && !TextUtils.isEmpty(msg)) {
            val toast = Toast(context)
            val v: View = LayoutInflater.from(context).inflate(R.layout.bean_get_notice_view, null, false)
            val tv = v.findViewById<View>(R.id.toast_tips) as TextView
            tv.text = msg
            val suffix = v.findViewById<View>(R.id.suffix) as TextView
            count = "+$count"
            suffix.text = count
            toast.setMargin(0f, 0f)
            toast.view = v
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_SHORT
            hookToast(toast)
            toast.show()
        }
    }

    @JvmStatic fun showVideoTipToast(context: Context?, view: View?, duration: Int, yOffset: Int) {
        if (context != null && view != null) {
            val toast = Toast(context)
            toast.setMargin(0f, 0f)
            toast.view = view
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, yOffset)
            toast.duration = duration
            hookToast(toast)
            /*ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, 0);
            animator.setDuration(duration);
            animator.start();*/toast.show()
        }
    }

    @JvmStatic fun showCenterToast(context: Context?, msg: String?) {
        if (context != null && !TextUtils.isEmpty(msg)) {
            val toast = Toast(context)
            val v: View = LayoutInflater.from(context).inflate(R.layout.center_notice_view, null, false)
            val tv = v.findViewById<View>(R.id.toast_tips) as TextView
            tv.text = msg
            toast.setMargin(0f, 0f)
            toast.view = v
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_SHORT
            hookToast(toast)
            toast.show()
        }
    }

    //refer: https://blog.csdn.net/okg0111/article/details/83957680
    private var sField_TN: Field? = null
    private var sField_TN_Handler: Field? = null
    private var sIsHookFieldInit = false
    private const val FIELD_NAME_TN = "mTN"
    private const val FIELD_NAME_HANDLER = "mHandler"
    private fun hookToast(toast: Toast?) {
        try {
            if (!sIsHookFieldInit) {
                sField_TN = Toast::class.java.getDeclaredField(FIELD_NAME_TN)
                sField_TN?.let {
                    sField_TN!!.isAccessible = true
                    sField_TN_Handler = sField_TN!!.type.getDeclaredField(FIELD_NAME_HANDLER)
                    sField_TN_Handler?.isAccessible = true
                    sIsHookFieldInit = true
                }
            }
            val tn = sField_TN!![toast]
            val originHandler = sField_TN_Handler!![tn] as Handler
            sField_TN_Handler!![tn] = SafelyHandlerWrapper(originHandler)
        } catch (e: Throwable) {
            Log.e(TAG, "Hook toast exception=$e")
        }
    }

    /**
     * Safe outside Handler class which just warps the system origin handler object in the Toast.class
     */
    private class SafelyHandlerWrapper(private val originHandler: Handler?) : Handler() {
        override fun dispatchMessage(msg: Message) {
            // The outside handler SafelyHandlerWrapper object just catches the Exception while dispatch the message
            // if the the inside system origin handler object throw the BadTokenException，the outside safe SafelyHandlerWrapper object
            // just catches the exception here to avoid the app crashing
            try {
                super.dispatchMessage(msg)
            } catch (e: Throwable) {
                Log.e(TAG, "Catch system toast exception:$e")
            }
        }

        override fun handleMessage(msg: Message) {
            //just pass the Message to the origin handler object to handle
            originHandler?.handleMessage(msg)
        }
    }
}