package com.study.lib.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.study.lib.common.R

object LoadingDialogUtil {
    private const val TAG = "DialogUtil"

    fun showLoadingDialog(context: Context, message: String?, cancelable: Boolean): Dialog? {
        var dialog: Dialog? = null
        try {
            val alertView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
            // OutOfMemoryError
            try {
                (alertView.findViewById<View>(R.id.dialog_loading_progress) as ProgressBar).indeterminateDrawable = context.resources.getDrawable(R.drawable.babyhealth_loading)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            dialog = Dialog(context, R.style.CustomDialog)
            dialog.setContentView(alertView)
            val layout = alertView.findViewById<View>(R.id.dialog_loading_layout)
            val msgView = alertView.findViewById<View>(R.id.dialog_loading_message) as TextView
            if (!TextUtils.isEmpty(message)) {
                msgView.text = message
                msgView.visibility = View.VISIBLE
                try {
                    layout.setBackgroundResource(R.drawable.dialog_cheer_bg)
                } catch (e: Throwable) {
                    Log.e(TAG, "showLoadingDialog e[$e]")
                }
            } else {
                msgView.visibility = View.GONE
            }
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(cancelable)
            if (!dialog.isShowing && !(context as Activity).isFinishing) {
                try {
                    dialog.show()
                } catch (e: Throwable) {
                    Log.e(TAG, "showLoadingDialog e[$e]")
                }
            }
        } catch (e: Throwable) {
            Log.e(TAG, "showLoadingDialog e[$e]")
        }
        return dialog
    }

    fun dismissLoadingDialog(dialog: Dialog?) {
        try {
            dialog?.dismiss()
        } catch (r: Throwable) {
            r.printStackTrace()
        }
    }
}