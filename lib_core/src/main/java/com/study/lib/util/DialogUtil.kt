package com.study.lib.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.study.lib.core.R

class DialogUtil {
    var dialog: Dialog? = null

    fun showDialog(context: Context?, message: String) {
        if (context is Activity) {
            val alertView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
            dialog = Dialog(context, R.style.loading_dialog)
            dialog?.setContentView(alertView)
            val msgText = alertView.findViewById<TextView>(R.id.tv_text)
            msgText?.text = message
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.setCancelable(true)
            try {
                dialog?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun dismiss() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }
}
