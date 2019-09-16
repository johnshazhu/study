package com.drcuiyutao.lib.gson

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object TypeUtil {
    fun type(raw: Class<*>, vararg args: Type): ParameterizedType {
        return object : ParameterizedType {
            override fun getRawType(): Type {
                return raw
            }

            override fun getActualTypeArguments(): Array<Type> {
                return arrayOf(*args)
            }

            override fun getOwnerType(): Type? {
                return null
            }
        }
    }
}