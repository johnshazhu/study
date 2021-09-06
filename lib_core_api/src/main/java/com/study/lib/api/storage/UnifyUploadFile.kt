package com.study.lib.api.storage

import com.study.lib.api.APIBaseRequest
import com.study.lib.api.APIConfig1
import com.study.lib.api.BaseResponseData
import com.study.lib.api.storage.UnifyUploadFile.UnifyUploadFileRsp

class UnifyUploadFile(var bizNo: String, var fileKeyList: List<String>, val files: List<String>) :
        APIBaseRequest<UnifyUploadFileRsp>() {
    override var url: String
        get() = "${APIConfig1.NEW_UPLOAD_BASE}/storage/unifyUploadFile"
        set(value) {
            super.url = value
        }

    inner class UnifyUploadFileRsp : BaseResponseData() {
        var urlList: List<String>? = null
        var keyList: List<String>? = null
    }
}