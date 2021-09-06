package com.study.lib.api

import com.study.lib.api.v66.NewAPIBaseRequest
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by john on 2018/11/15.
 */
class TransformRequest<T : BaseResponseData>(private val delegateUrl: String, listener: APIBase.ResponseListener<T>?) : NewAPIBaseRequest<T>() {
    var genericType: Type? = null
    override var url: String
        get() = delegateUrl
        set(value) {
            super.url = value
        }

    init {
        if (listener != null) {
            var clazz: Class<*>? = listener.javaClass
            while (clazz != null) {
                val types = clazz.genericInterfaces
                if (types.isNotEmpty()) {
                    for (type in types) {
                        if (type !is ParameterizedType) {
                            continue
                        }
                        val targetType = type.actualTypeArguments[0]
                        if (BaseResponseData::class.java.isAssignableFrom(targetType as Class<*>)) {
                            genericType = targetType
                            break
                        }
                    }
                }
                clazz = clazz.superclass
            }
        }
    }
}