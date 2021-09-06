package com.study.lib.json

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

object JsonUtil {
    private var sType: Type? = null
    private var sTypeAdapter: Class<*>? = null
    @JvmStatic
    fun setTypeAndTypeAdapter(type: Type?, typeAdapter: Class<*>?) {
        sType = type
        sTypeAdapter = typeAdapter
    }

    val gsonBuilder: GsonBuilder
        get() {
            val builder = GsonBuilder()
            if (sType != null && sTypeAdapter != null) {
                builder.registerTypeAdapter(sType, sTypeAdapter?.getConstructor()?.newInstance())
            }
            return builder
        }

    val gson = gsonBuilder.create()

    @Throws(JsonSyntaxException::class)
    @JvmStatic
    fun <T> fromJson(json: String?, classOfT: Class<T>?): T {
        return gson.fromJson(json, classOfT)
    }

    @Throws(JsonSyntaxException::class)
    @JvmStatic
    fun <T> fromJson(json: JsonElement?, classOfT: Class<T>?): T {
        return gsonBuilder.create().fromJson(json, classOfT)
    }

    @Throws(JsonSyntaxException::class)
    @JvmStatic
    fun <T> fromJson(json: String?, typeOfT: Type?): T {
        return gson.fromJson(json, typeOfT)
    }

    @JvmStatic
    fun toJson(src: Any?): String {
        return gson.toJson(src)
    }
}