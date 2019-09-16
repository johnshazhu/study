package com.drcuiyutao.lib.api.base

import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.RequestBody
import retrofit2.Converter

import java.io.IOException

class RequestBodyConverter<T> : Converter<T, RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: T): RequestBody? {
        return getFormBody<T>(value)
    }

    private fun <T> getFormBody(value: T?): FormBody {
        val builder = FormBody.Builder()

        value?.let {
            builder.add("body", Gson().toJson(value))
        }

        return builder.build()
    }
}
