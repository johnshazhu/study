package com.study.lib.ui

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import com.study.lib.api.LoadingCancelListener
import com.study.lib.util.LoadingDialogUtil
import com.study.lib.util.ToastUtil

class UIController(val context: Context,
                   showLoading: Boolean,
                   closeLoadingWhenFinished: Boolean,
                   showToastWhenFailed: Boolean,
                   loadingMsg: String?) {
    var isShowLoading = true
    var isCloseLoadingWhenFinished = true
    var isChanged = false
    var isShowToastWhenFailed = true
    var cancelable = true
    var loadingMsg: String?
    var toastMsg: String? = null
    var isShow = false
    @Transient
    var listener: UIUpdateListener? = null
    @Transient
    var cancelListener: LoadingCancelListener? = null
    var isCanceled = false
    @Transient
    var dialog: Dialog? = null

    constructor(context: Context, closeLoadingWhenFinished: Boolean) :
            this(context, true, closeLoadingWhenFinished,
                    true, null)
    constructor(context: Context, loadingMsg: String?) :
            this(context, true, true,
                    true, loadingMsg)

    fun setToastMsgId(strId: Int) {
        toastMsg = context.resources.getString(strId)
    }

    fun start() {
        if (isShowLoading && !isShow) {
            isShow = true
            isCanceled = false
            dialog = if (TextUtils.isEmpty(loadingMsg)) {
                LoadingDialogUtil.showLoadingDialog(context, null, cancelable)
            } else {
                LoadingDialogUtil.showLoadingDialog(context, loadingMsg, cancelable)
            }
            dialog!!.setOnCancelListener {
                isCanceled = true
                if (cancelListener != null) {
                    cancelListener!!.cancel()
                }
                cancelListener = null
            }
        }
    }

    fun stop(closeWhenFailed: Boolean) {
        if (isCloseLoadingWhenFinished || closeWhenFailed) {
            isShow = false
            LoadingDialogUtil.dismissLoadingDialog(dialog)
        }
        if (isShowToastWhenFailed && !TextUtils.isEmpty(toastMsg) && !isCanceled) {
            ToastUtil.show(context, toastMsg, ToastUtil.TOAST_DURATION)
        }
    }

    init {
        isShowLoading = showLoading
        this.isCloseLoadingWhenFinished = closeLoadingWhenFinished
        isShowToastWhenFailed = showToastWhenFailed
        this.loadingMsg = loadingMsg
    }
}