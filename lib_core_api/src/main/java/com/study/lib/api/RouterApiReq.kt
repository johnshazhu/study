package com.study.lib.api

import com.study.lib.api.APIBaseRequest
import com.study.lib.api.APIEmptyResponseData

class RouterApiReq(url: String) : APIBaseRequest<APIEmptyResponseData>() {
    init {
        this.url = url
    }
}