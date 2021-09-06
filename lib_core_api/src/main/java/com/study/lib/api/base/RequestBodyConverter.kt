package com.study.lib.api.base

import com.study.lib.api.base.RetrofitBase.Companion.getFormBody
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.IOException

class RequestBodyConverter<T> : Converter<T, RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        return getFormBody(value)
    }
}