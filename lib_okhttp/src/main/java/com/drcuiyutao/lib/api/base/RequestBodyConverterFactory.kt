package com.drcuiyutao.lib.api.base

import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class RequestBodyConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type?, parameterAnnotations: Array<Annotation>?,
        methodAnnotations: Array<Annotation>?, retrofit: Retrofit?
    ): Converter<*, RequestBody> {
        return RequestBodyConverter<Any>()
    }

    companion object {
        private val TAG = RequestBodyConverterFactory::class.java.simpleName

        fun create(): RequestBodyConverterFactory {
            return RequestBodyConverterFactory()
        }
    }
}