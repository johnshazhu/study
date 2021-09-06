package com.study.lib.api

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.webkit.MimeTypeMap
import com.study.lib.api.APIBase.getSignHeader
import com.study.lib.api.APIUtils.commonHeaders
import com.study.lib.api.APIUtils.getRequestParams
import com.study.lib.api.base.OkHttpRequestUtil
import com.study.lib.api.base.ProgressBody
import com.study.lib.api.base.ResponseHandlerUtil.parseResponse
import com.study.lib.api.base.RetrofitBase
import com.study.lib.api.base.RetrofitBase.Companion.client
import com.study.lib.api.storage.UnifyUploadFile
import com.study.lib.log.LogUtil.debug
import com.study.lib.log.LogUtil.i
import com.study.lib.R
import com.study.lib.ui.UIController
import com.study.lib.ui.UIUpdateListener
import com.study.lib.util.InjectUtil
import com.study.lib.util.SimpleUtil
import com.study.lib.util.ToastUtil
import okhttp3.*
import java.io.File
import java.util.*

abstract class APIBaseRequest<T : BaseResponseData> {
    @OmittedAnnotation
    var host: String? = null

    @OmittedAnnotation
    open lateinit var url: String

    @OmittedAnnotation
    @Transient
    var response: APIBaseResponse<T>? = null

    @OmittedAnnotation
    @Transient
    var rspListener: APIBase.ResponseListener<T>? = null

    @OmittedAnnotation
    @Transient
    var controller: UIController? = null

    @OmittedAnnotation
    @Transient
    var uiListener: UIUpdateListener? = null

    @OmittedAnnotation
    @Transient
    var subRequest: APIBaseRequest<*>? = null

    @OmittedAnnotation
    @Transient
    var subRspListener: APIBase.ResponseListener<*>? = null

    @OmittedAnnotation
    var autoToastWhenFailed = true

    @OmittedAnnotation
    var inThread = false

    @OmittedAnnotation
    var requestStartTimestamp: Long = 0

    @RequestBodyForm
    var body: String? = null

    @OmittedAnnotation
    @Transient
    var progressListener: ProgressBody.ProgressListener? = null

    var memberId2: String? = null
    var childId: String? = null
    var uid = 0
    var cts: Long = -1
    var traceLogId: String? = null
    var appDevice: BaseRequestData? = null

    init {
        appDevice = BaseRequestData.instance
        uid = appDevice!!.userID
        traceLogId = UUID.randomUUID().toString()
        updateChildInfo()
        requestStartTimestamp = System.currentTimeMillis()
    }

    fun updateChildInfo() {
        memberId2 = BaseRequestData.instance.memberId2
        childId = BaseRequestData.instance.childId
    }

    fun post(listener: APIBase.ResponseListener<T>?) {
        controller?.start()
        request(listener)
    }

    fun postSubRequest(listener: APIBase.ResponseListener<*>?) {
        request(listener as APIBase.ResponseListener<T>)
    }

    open fun postWithoutAutoToast(listener: APIBase.ResponseListener<T>?) {
        autoToastWhenFailed = false
        post(listener)
    }

    fun request(listener: APIBase.ResponseListener<T>?) {
        getSignHeader(url, commonHeaders, null)
        val params = getRequestParams(this)
        val bodyBuilder = FormBody.Builder()
        if (params?.isNotEmpty() == true) {
            for (pair in params) {
                bodyBuilder.add(pair.name, pair.value)
            }
        }
        rspListener = listener
        requestStartTimestamp = System.currentTimeMillis()
        val builder = Request.Builder()
                .url(url)
                .method(RetrofitBase.METHOD_POST, bodyBuilder.build())
                .tag(RetrofitBase.NO_DEFAULT_BODY)
        if (host?.isNotEmpty() == true) {
            builder.addHeader(RetrofitBase.HOST, host!!)
        }

        okHttpCall(client, builder.build())


        /*val call = RetrofitBase.client.newCall(request)
        try {
            val response = call.execute()
            ResponseHandlerUtil.parseResponse(call, response, response.body()!!.string(), this)
        } catch (e: Throwable) {
            e.printStackTrace()
        }*/
    }

    open fun requestWithoutHeader(body: RequestBody?, listener: APIBase.ResponseListener<T>?) {
        val client = client
        val request: Request = Request.Builder()
                .url(url)
                .method(RetrofitBase.METHOD_POST, body)
                .tag(RetrofitBase.NO_DEFAULT_BODY)
                .build()
        rspListener = listener
        okHttpCall(client, request)
    }

    /**
     * 展示加载框的请求, 默认无网络时弹toast
     *
     * @param context  用于网络状态判断和加载框显示
     * @param listener 请求回调接口, 为null表示不关心结果
     */
    open fun request(context: Context?, listener: APIBase.ResponseListener<T>?) {
        request(context, true, listener)
    }

    /**
     * 展示加载框的请求, 失败时弹窗提示, 不关注UI状态
     *
     * @param context            用于网络状态判断和加载框显示
     * @param showNoNetworkToast true 无网络时toast提示 false 不提示
     * @param listener           请求回调接口, 为null表示不关心结果
     */
    open fun request(context: Context?,
                     showNoNetworkToast: Boolean,
                     listener: APIBase.ResponseListener<T>?) {
        request(context, showNoNetworkToast, null, true, true, true, listener)
    }

    /**
     * 最简单情况 : 不展示加载框
     *
     * @param listener 请求回调接口, 为null表示不关心结果
     */
    open fun requestWithoutLoading(listener: APIBase.ResponseListener<T>?) {
        if (SimpleUtil.hasNetwork(InjectUtil.context()!!)) {
            request(listener)
        } else listener?.onFailure(0, null)
    }

    /**
     * 展示加载框的请求, 失败时弹窗提示, 不关注UI状态, 加载过程中不可取消
     *
     * @param context  用于网络状态判断和加载框显示
     * @param listener 请求回调接口, 为null表示不关心结果
     */
    open fun requestCannotCancel(context: Context?, listener: APIBase.ResponseListener<T>?) {
        requestCannotCancel(context, null, listener)
    }

    /**
     * 最简单情况 : 不展示加载框 不判断是否有网络
     *
     * @param listener 请求回调接口, 为null表示不关心结果
     */
    open fun requestWithoutLoadingNetwork(context: Context?, listener: APIBase.ResponseListener<T>?) {
        if (SimpleUtil.hasNetwork(InjectUtil.context()!!)) {
            if (context != null) {
                request(context, true, true, false, listener)
            } else {
                request(listener)
            }
        } else listener?.onFailure(0, null)
    }

    /**
     * 上拉下拉时通用接口
     *
     * @param context            用以判断网络状态
     * @param isLoadMore         是否加载更多 true 是(上拉) false 否(下拉)
     * @param showNoNetworkToast true 无网络时toast提示 false 不提示
     * @param uiListener         无网络或者数据监听接口, 为null时表示不关心ui
     * @param listener           请求回调接口, 为null表示不关心结果
     */
    open fun requestWithDirection(context: Context?,
                                  isLoadMore: Boolean,
                                  showNoNetworkToast: Boolean,
                                  uiListener: UIUpdateListener?,
                                  listener: APIBase.ResponseListener<T>?) {
        var context = context
        if (context == null) {
            context = InjectUtil.context()!!
        }
        if (SimpleUtil.hasNetwork(context)) {
            this.uiListener = uiListener
            if (!isLoadMore && context is Activity) {
                controller = UIController(context, true, true, true, null)
                controller!!.cancelable = true
                controller!!.start()
            }
            uiListener?.hideRefreshView()
            request(listener)
        } else {
            if (showNoNetworkToast) {
                ToastUtil.show(InjectUtil.context()!!, R.string.no_network)
            }
            if (uiListener != null) {
                if (!isLoadMore) {
                    uiListener.showConnectExceptionView(false)
                }
                uiListener.releaseToRefresh()
            }
            listener?.onFailure(-1, null)
        }
    }

    /**
     * 展示加载框的请求, 失败时弹窗提示, 不关注UI状态
     *
     * @param context            用于网络状态判断和加载框显示
     * @param showNoNetworkToast true 无网络时toast提示 false 不提示
     * @param closeWhenFinished  true 结束时关闭加载框 false 不关闭
     * @param listener           请求回调接口, 为null表示不关心结果
     */
    open fun request(context: Context?,
                     showNoNetworkToast: Boolean,
                     closeWhenFinished: Boolean,
                     listener: APIBase.ResponseListener<T>?) {
        request(context, showNoNetworkToast, null, true, closeWhenFinished, true, listener)
    }

    /**
     * 展示加载框的请求, 失败时弹窗提示, 不关注UI状态
     *
     * @param context             用于网络状态判断和加载框显示
     * @param cancelable          true 加载框可取消 false 不可取消
     * @param closeWhenFinished   true 结束时关闭加载框 false 不关闭
     * @param showToastWhenFailed true 失败时toast提示 false 不提示
     * @param listener            请求回调接口, 为null表示不关心结果
     */
    open fun request(context: Context?,
                     cancelable: Boolean,
                     closeWhenFinished: Boolean,
                     showToastWhenFailed: Boolean,
                     listener: APIBase.ResponseListener<T>?) {
        request(context, true, null, cancelable, closeWhenFinished, showToastWhenFailed, listener)
    }

    /**
     * 展示加载框的请求 : 默认请求结束自动关闭加载框, 失败时弹toast提示, 加载过程中禁止取消
     *
     * @param context    用于网络状态判断和加载框显示
     * @param uiListener 无网络或者数据监听接口, 为null时表示不关心ui
     * @param listener   请求回调接口, 为null表示不关心结果
     */
    open fun requestCannotCancel(context: Context?,
                                 uiListener: UIUpdateListener?,
                                 listener: APIBase.ResponseListener<T>?) {
        request(context, true, uiListener, false, true, true, listener)
    }

    /**
     * 展示加载框的请求
     *
     * @param context            用于网络状态判断和加载框显示
     * @param showNoNetworkToast true 无网络时toast提示 false 不提示
     * @param uiListener         无网络或者数据监听接口, 为null时表示不关心ui
     * @param listener           请求回调接口, 为null表示不关心结果
     */
    open fun requestCloseWhenFinished(context: Context?,
                                      showNoNetworkToast: Boolean,
                                      uiListener: UIUpdateListener?,
                                      listener: APIBase.ResponseListener<T>?) {
        request(context, showNoNetworkToast, uiListener, true, false, true, listener)
    }

    /**
     * 展示加载框的请求
     *
     * @param context            用于网络状态判断和加载框显示
     * @param showNoNetworkToast true 无网络时toast提示 false 不提示
     * @param uiListener         无网络或者数据监听接口, 为null时表示不关心ui
     * @param listener           请求回调接口, 为null表示不关心结果
     */
    open fun requestWithoutFailureToast(context: Context?,
                                        showNoNetworkToast: Boolean,
                                        uiListener: UIUpdateListener?,
                                        listener: APIBase.ResponseListener<T>?) {
        request(context, showNoNetworkToast, uiListener, true, true, false, listener)
    }

    /**
     * 展示加载框的请求 : 加载过程中可关闭, 请求结束自动关闭加载框, 失败时弹toast提示
     *
     * @param context    用于网络状态判断和加载框显示
     * @param uiListener 无网络或者数据监听接口, 为null时表示不关心ui
     * @param listener   请求回调接口, 为null表示不关心结果
     */
    open fun request(context: Context?,
                     uiListener: UIUpdateListener?,
                     listener: APIBase.ResponseListener<T>?) {
        request(context, true, uiListener, true, true, true, listener)
    }

    /**
     * 展示加载框的请求 : 加载过程中可关闭, 请求结束自动关闭加载框, 失败时弹toast提示
     *
     * @param context            用于网络状态判断和加载框显示
     * @param showNoNetworkToast true 无网络时toast提示 false 不提示
     * @param uiListener         无网络或者数据监听接口, 为null时表示不关心ui
     * @param listener           请求回调接口, 为null表示不关心结果
     */
    open fun request(context: Context?,
                     showNoNetworkToast: Boolean,
                     uiListener: UIUpdateListener?,
                     listener: APIBase.ResponseListener<T>?) {
        request(context, showNoNetworkToast, uiListener, true, true, true, listener)
    }

    /**
     * 展示加载框的请求
     *
     * @param context             用于网络状态判断和加载框显示
     * @param showNoNetworkToast  true 无网络时toast提示 false 不提示
     * @param uiListener          无网络或者数据监听接口, 为null时表示不关心ui
     * @param cancelable          true 加载框可取消 false 不可取消
     * @param closeWhenFinished   true 结束时关闭加载框 false 不关闭
     * @param showToastWhenFailed true 失败时toast提示 false 不提示
     * @param listener            请求回调接口, 为null表示不关心结果
     */
    open fun request(context: Context?,
                     showNoNetworkToast: Boolean,
                     uiListener: UIUpdateListener?,
                     cancelable: Boolean,
                     closeWhenFinished: Boolean,
                     showToastWhenFailed: Boolean,
                     listener: APIBase.ResponseListener<T>?) {
        var context = context
        if (context == null) {
            context = InjectUtil.context()!!
        }
        if (SimpleUtil.hasNetwork(context)) {
            if (context is Activity) {
                controller = UIController(context, true, closeWhenFinished, showToastWhenFailed, null)
                controller!!.cancelable = cancelable
                controller!!.start()
            }
            this.uiListener = uiListener
            uiListener?.hideRefreshView()
            request(listener)
        } else {
            if (showNoNetworkToast) {
                ToastUtil.show(context, R.string.no_network)
            }
            uiListener?.let {
                uiListener!!.showConnectExceptionView(false)
                uiListener!!.releaseToRefresh()
            }
        }
    }

    open fun postInThread(listener: APIBase.ResponseListener<T>?) {
        inThread = true
        debug("postInThread url : $url")
        post(listener)
    }

    open fun postFiles(context: Context?, showLoading: Boolean, showToast: Boolean, listener: APIBase.ResponseListener<T>?) {
        if (SimpleUtil.hasNetwork(context)) {
            if (context is Activity) {
                controller = UIController(context, showLoading, true, showToast, null)
                controller!!.cancelable = false
                controller!!.start()
            }
            val builder = MultipartBody.Builder()
            var list: List<String?>? = null
            var name: String? = null
            if (this is UnifyUploadFile) {
                builder.setType(MultipartBody.FORM)
                getSignHeader(url, commonHeaders, null)
                val params = getRequestParams(this)
                if (params?.isNotEmpty() == true) {
                    for (pair in params!!) {
                        builder.addFormDataPart(pair.name, pair.value)
                    }
                }
                val uploadFile: UnifyUploadFile = this as UnifyUploadFile
                builder.addFormDataPart("bizNo", uploadFile.bizNo)
                val fileKeyList = StringBuilder()
                val keys: List<String>? = uploadFile.fileKeyList
                val count: Int = keys?.size ?: 0
                if (count > 0) {
                    for (i in 0 until count) {
                        if (0 != i) {
                            fileKeyList.append(",")
                        }
                        fileKeyList.append(keys!![i])
                    }
                    builder.addFormDataPart("fileKeyList", fileKeyList.toString())
                }
                list = uploadFile.files
                name = "files"
            }
            i(TAG, "postFiles list[$list]")
            if (list?.isNotEmpty() == true) {
                for (path in list!!) {
                    setUploadFileBuilder(builder, File(path), name!!)
                }
            }
            val reqBuilder = Request.Builder()
            reqBuilder.url(url).post(builder.build())
            val client = client
            rspListener = listener
            okHttpCall(client, reqBuilder.build())
        } else {
            if (showToast) {
                ToastUtil.show(context!!, R.string.no_network)
            }
            listener?.onFailure(-1, null)
        }
    }

    open fun postFiles(context: Context?, listener: APIBase.ResponseListener<T>?) {
        postFiles(context, true, true, listener)
    }

    /**
     * 文件上传接口, 默认无网络时弹提示
     *
     * @param listener 请求回调接口, 为null表示不关心结果
     */
    open fun postFile(listener: APIBase.ResponseListener<T>?) {
        postFile(true, listener)
    }

    /**
     * 文件上传接口
     *
     * @param showToast 是否展示无网络提示, true 展示 false 不展示
     * @param listener  请求回调接口, 为null表示不关心结果
     */
    open fun postFile(showToast: Boolean, listener: APIBase.ResponseListener<T>?) {
        if (SimpleUtil.hasNetwork(InjectUtil.context()!!)) {
            val params = APIUtils.getPostRequestParams(this)
            val headers = getSignHeader(url, commonHeaders, params)
            val builder = MultipartBody.Builder()

            // Add string params
            if (params?.isNotEmpty() == true) {
                for (elem in params!!) {
                    builder.addFormDataPart(elem.name, elem.value)
                }
            }
            var clazz: Class<*>? = this.javaClass
            while (clazz != null) {
                val fields = clazz.declaredFields
                if (fields == null || fields.isEmpty()) {
                    clazz = clazz.superclass
                    continue
                }

                for (f in fields) {
                    f.isAccessible = true
                    try {
                        val obj = f.get(this)
                        if (obj == null || !(obj is File || obj is List<*>)) {
                            continue
                        }
                        if (obj is List<*> && obj.isNotEmpty()) {
                            if (obj[0] is File) {
                                // Add file params
                                for (elem in obj) {
                                    setUploadFileBuilder(builder, (elem as File?)!!, "image")
                                }
                            }
                        } else if (obj is File) {
                            setUploadFileBuilder(builder, obj, "image")
                        }
                    } catch (r: Throwable) {
                        r.printStackTrace()
                    }
                }
            }

            val headerBuilder = Headers.Builder()
            if (headers?.isNotEmpty() == true) {
                for (header in headers!!) {
                    headerBuilder.add(header!!.name, header!!.value)
                }
            }
            val reqBuilder = Request.Builder()
                    .url(url)
                    .headers(headerBuilder.build())
                    .post(builder.build())
            rspListener = listener
            okHttpCall(client, reqBuilder.build())
        } else if (showToast) {
            ToastUtil.show(InjectUtil.context()!!, R.string.no_network)
        }
    }

    open fun syncPost(listener: APIBase.ResponseListener<T>?) {
        val client = client
        updateChildInfo()
        getSignHeader(url, commonHeaders, null)
        val params = getRequestParams(this)
        val bodyBuilder = FormBody.Builder()
        if (params?.isNotEmpty() == true) {
            for (pair in params!!) {
                bodyBuilder.add(pair.name, pair.value)
            }
        }
        rspListener = listener
        requestStartTimestamp = System.currentTimeMillis()
        val request: Request = Request.Builder()
                .url(url)
                .method(RetrofitBase.METHOD_POST, bodyBuilder.build())
                .tag(RetrofitBase.NO_DEFAULT_BODY)
                .build()
        val call = client.newCall(request)
        try {
            val response = call.execute()
            parseResponse(call, response, response.body()!!.string(), this)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun okHttpCall(client: OkHttpClient, request: Request) {
        val call = client.newCall(request)
        controller?.cancelListener = object : LoadingCancelListener {
            override fun cancel() {
                call.cancel()
                if (call.isCanceled) {
                    debug("okHttpCall canceled : ${request.url()}")
                    uiListener?.canceled()
                }
            }
        }
        requestStartTimestamp = System.currentTimeMillis()
        debug("request inThread $inThread, url : $url")
        OkHttpRequestUtil(inThread).request(call, this)
    }

    private fun setUploadFileBuilder(builder: MultipartBody.Builder, file: File, name: String) {
        val extension: String = SimpleUtil.getFileExtensionFromUrl(file.absolutePath)
        var fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        i(TAG, "setUploadFileBuilder extension[$extension] fileType[$fileType]")
        if (TextUtils.isEmpty(fileType)) {
            fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")
            i(TAG, "setUploadFileBuilder new fileType[$fileType]")
        }
        if (fileType != null && file.exists()) {
            val mediaType = MediaType.parse(fileType)
            if (progressListener != null) {
                builder.addFormDataPart(name, file.name,
                        ProgressBody(file, if (mediaType == null) "" else mediaType.type(), progressListener)
                )
            } else {
                builder.addFormDataPart(name, file.name,
                        RequestBody.create(MediaType.parse(fileType), file))
            }
        }
    }

    protected open fun onSuccess() {}

    protected open fun onFail() {}

    open fun <T : BaseResponseData> onSuccess(request: APIBaseRequest<T>) {
        request.onSuccess()
    }

    open fun <T : BaseResponseData> onFail(request: APIBaseRequest<T>) {
        request.onFail()
    }

    companion object {
        const val TAG = "APIBaseRequest"
    }
}