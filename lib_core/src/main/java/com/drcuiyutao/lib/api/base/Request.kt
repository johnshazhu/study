package com.drcuiyutao.lib.api.base

import android.content.Context
import com.drcuiyutao.lib.gson.GsonUtil
import com.drcuiyutao.lib.sys.BaseApplication
import com.drcuiyutao.lib.util.DialogUtil
import com.drcuiyutao.lib.util.LogUtil
import com.drcuiyutao.lib.util.NetworkUtil
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException

object Request {
    fun <T : BaseResponseData> call(context: Context?, flowable: Flowable<APIBaseResponse<T>>, listener: ResponseListener<APIBaseResponse<T>>?) {
        val hasNetwork = NetworkUtil.hasNetwork(context?: BaseApplication.getContext())
        if (!hasNetwork) {
            return
        }

        val dialog = DialogUtil()
        val result = flowable.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ rsp ->
                dialog.dismiss()
                rsp?.let {
                    LogUtil.debug(GsonUtil.getString(rsp))
                    if (rsp.isWxNoUnionID) {
                        //TODO : 微信授权过期
                        return@let
                    }

                    //TODO : token过期处理

                    if (rsp.isSuccess && rsp.yd > 0) {
                        //TODO : 园豆toast处理
                    }

                    listener?.let {
                        if (rsp.isSuccess) {
                            listener.onBizSuccess(rsp)
                        } else {
                            //TODO : 失败toast处理
                            listener.onBizFailure(rsp)
                        }
                    }
                }
            }, { throwable ->
                dialog.dismiss()
                if (throwable is HttpException) {
                    listener?.onFailure(throwable.code(), throwable.message())
                    LogUtil.debug("failed : " + GsonUtil.getString(throwable.response()))
                } else {
                    listener?.onFailure(0, throwable.message)
                }
            })

        dialog.showDialog(context, "加载中...")
        dialog.dialog?.setOnCancelListener { _ ->
            LogUtil.debug("cancel request on close dialog")
            result?.dispose()
            if (result.isDisposed) {
                LogUtil.debug("result.isDisposed : ${result.isDisposed}")
            }
        }
    }
}
