package com.study.lib.api.base

import okhttp3.RequestBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class RequestBodyConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>,
                                      methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        return RequestBodyConverter<Any>()
    }

    companion object {
        fun create(): RequestBodyConverterFactory {
            return RequestBodyConverterFactory()
        }
    }
}