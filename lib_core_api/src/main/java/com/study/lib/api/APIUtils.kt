package com.study.lib.api

import android.content.Context
import android.content.pm.PackageInfo
import android.text.TextUtils
import com.getkeepsafe.relinker.ReLinker
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.study.lib.util.*
import org.json.JSONObject
import java.io.*
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

object APIUtils {
    const val APP_DEVICE = "appDevice"
    const val TIME = "t"
    const val DEVICE_NO = "deviceno"
    const val SIGN = "sign"
    const val INVALID_SIGNATURE = "invalid_signature"
    const val INVALID_INTEGER_VALUE = -1

    private const val TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private const val PHOTO_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm"
    private const val DAY_LOG_TIMESTAMP_FORMAT = "yyyy-MM-dd"
    private val sExclusionStrategy = exclusionStrategy
    private val exclusionStrategy: ExclusionStrategy
        private get() = object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.getAnnotation<OmittedAnnotation>(OmittedAnnotation::class.java) != null ||
                        f.getAnnotation<RequestBodyForm>(RequestBodyForm::class.java) != null ||
                        f.hasModifier(Modifier.STATIC) ||
                        f.hasModifier(Modifier.TRANSIENT)
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }
        }

    @JvmStatic
    fun getRequestParams(request: APIBaseRequest<*>): List<NameValuePair>? {
        return getPostRequestParams(request)
    }

    @JvmStatic
    fun updateBodyString(body: String?): String? {
        var updateBody = body
        try {
            val jsonObject = JSONObject(body)
            val appDevice = jsonObject.optString(APP_DEVICE)
            if (!TextUtils.isEmpty(appDevice)) {
                val device = JSONObject(appDevice)
                device.put(TIME, System.currentTimeMillis())
                val headerList: MutableList<Header> = LinkedList<Header>()
                headerList.add(Header(DEVICE_NO, device.optString(DEVICE_NO)))
                headerList.add(Header(TIME, device.optString(TIME)))
                var sign: String? = null
                sign = nativeGetParam(InjectUtil.context()!!, headerList, null)
                if (INVALID_SIGNATURE == sign) {
                    sign = sign + "_" + SimpleUtil.getSignatureCode(InjectUtil.context()!!)
                }
                device.put(SIGN, sign)
                jsonObject.put(APP_DEVICE, device)
                updateBody = jsonObject.toString()
            }
        } catch (r: Throwable) {
            r.printStackTrace()
        }
        return updateBody
    }

    @JvmStatic
    fun getPostRequestParams(request: APIBaseRequest<*>): ArrayList<NameValuePair>? {
        val list: ArrayList<NameValuePair> = ArrayList<NameValuePair>()
        try {
            val gson = GsonBuilder().setExclusionStrategies(sExclusionStrategy).create()
            request.body = updateBodyString(gson.toJson(request))
        } catch (r: Throwable) {
            r.printStackTrace()
        }
        genFormParams(list, request)
        return if (list.isEmpty()) null else list
    }

    @JvmStatic
    val commonHeaders: Array<Header>?
        get() {
            val data = BaseRequestData.instance
            data.t = DateTimeUtil.currentTimestamp.toString()
            val fields = data.javaClass.declaredFields
            if (fields.isNotEmpty()) {
                return null
            }
            val list: MutableList<Header> = ArrayList()
            for (fieldItem in fields) {
                fieldItem.isAccessible = true
                try {
                    val obj = fieldItem.get(data)
                    if (obj == null || isFieldCanBeOmitted(fieldItem)) {
                        continue
                    }
                    if (obj is String) {
                        if (!TextUtils.isEmpty(obj)) {
                            list.add(Header(fieldItem.name, obj))
                        }
                    } else {
                        list.add(Header(fieldItem.name, obj.toString()))
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            return if (list.size == 0) null else list.toTypedArray()
        }

    private fun isParamExist(list: ArrayList<NameValuePair>, name: String): Boolean {
        for (pair in list) {
            if (pair.name == name) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isFieldCanBeOmitted(field: Field): Boolean {
        return field.isAnnotationPresent(OmittedAnnotation::class.java) ||
                Modifier.isStatic(field.modifiers) ||
                Modifier.isTransient(field.modifiers)
    }

    @JvmStatic
    fun isFieldForm(field: Field): Boolean {
        return field.isAnnotationPresent(RequestBodyForm::class.java)
    }

    private fun genParams(list: ArrayList<NameValuePair>, clsObject: Any) {
        var clazz: Class<*>? = clsObject.javaClass
        while (clazz != null) {
            val fields = clazz.declaredFields
            if (fields.isEmpty()) {
                clazz = clazz.superclass
                continue
            }
            for (fieldItem in fields) {
                fieldItem.isAccessible = true
                try {
                    val obj = fieldItem.get(clsObject)
                    if (obj == null || isFieldCanBeOmitted(fieldItem) || isParamExist(list, fieldItem.name)) {
                        continue
                    }
                    if (obj is String) {
                        if (!TextUtils.isEmpty(obj)) {
                            list.add(NameValuePair(fieldItem.name, obj))
                        }
                    } else if (obj is Float) {
                        if (obj > 0) {
                            list.add(NameValuePair(fieldItem.name, obj.toString()))
                        }
                    } else if (obj is Int) {
                        if (obj != SimpleUtil.INVALID_INTEGER_VALUE) {
                            list.add(NameValuePair(fieldItem.name, if (obj == Int.MIN_VALUE) SimpleUtil.INVALID_INTEGER_VALUE.toString() else obj.toString()))
                        }
                    } else if (obj is Long) {
                        if (obj != -1L) {
                            list.add(NameValuePair(fieldItem.name, obj.toString()))
                        }
                    } else if (obj is List<*> && obj.size > 0) {
                        if (obj[0] is String || obj[0] is Int) {
                            for (elem in obj) {
                                list.add(NameValuePair(fieldItem.name, elem.toString()))
                            }
                        }
                    } else if (obj !is File) {
                        list.add(NameValuePair(fieldItem.name, obj.toString()))
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
            clazz = clazz.superclass
        }
    }

    private fun genFormParams(list: ArrayList<NameValuePair>, clsObject: Any) {
        var clazz: Class<*>? = clsObject.javaClass
        while (clazz != null) {
            val fields = clazz.declaredFields
            if (fields.isEmpty()) {
                clazz = clazz.superclass
                continue
            }
            if (clazz.simpleName != APIBaseRequest::class.java.simpleName) {
                clazz = clazz.superclass
                continue
            }
            for (fieldItem in fields) {
                fieldItem.isAccessible = true
                try {
                    val obj = fieldItem.get(clsObject)
                    if (obj == null || !isFieldForm(fieldItem)) {
                        continue
                    }
                    list.add(NameValuePair(fieldItem.name, obj as String))
                } catch (r: Throwable) {
                    r.printStackTrace()
                }
            }
            clazz = clazz.superclass
        }
    }
    @JvmStatic
    fun getFormattedTimeStamp(time: Long): String {
        return SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault()).format(Date(time))
    }
    @JvmStatic
    fun getFormattedTimeStampNoSec(time: Long): String {
        return SimpleDateFormat(PHOTO_TIMESTAMP_FORMAT, Locale.getDefault()).format(Date(time))
    }

    @Throws(Exception::class)
    private fun loadFileAsString(fileName: String): String {
        val reader = FileReader(fileName)
        val text = loadReaderAsString(reader)
        reader.close()
        return text
    }

    @Throws(Exception::class)
    private fun loadReaderAsString(reader: Reader): String {
        val builder = StringBuilder()
        val buffer = CharArray(4096)
        var readLength = reader.read(buffer)
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength)
            readLength = reader.read(buffer)
        }
        return builder.toString()
    }

    @JvmStatic
    val appChannel: String?
        get() {
            val context: Context = InjectUtil.context()!!
            try {
                return ChannelUtil.getChannel(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

    // TransactionTooLargeException
    @JvmStatic
    val appVersion: String?
        get() {
            var packInfo: PackageInfo? = null
            val context: Context = InjectUtil.context()!!
            try {
                packInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            } catch (e: Throwable) {
                // TransactionTooLargeException
                e.printStackTrace()
            }
            return packInfo?.versionName
        }

    @JvmStatic
    val UUID: String?
        get() {
            return UUIDUtil.getUUID()
        }

    @JvmStatic
    val MACAddress: String?
        get() {
            return SimpleUtil.getMacAddress()
        }

    @JvmStatic
    fun getTimeByFormat(time: String?): Long {
        return try {
            SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault()).parse(time).time
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    @JvmStatic
    fun getDaylogTimeFormat(time: Long): String {
        return SimpleDateFormat(DAY_LOG_TIMESTAMP_FORMAT, Locale.getDefault()).format(Date(time))
    }

    @JvmStatic
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

    external fun nativeGetParam(context: Context?, headers: List<Header>?, list: List<NameValuePair>?): String
    external fun nativeGetImPassword(context: Context?, key: String?): String

    init {
        try {
            System.loadLibrary("api_encrypt")
        } catch (e: Throwable) {
            try {
                ReLinker.loadLibrary(InjectUtil.context()!!, "api_encrypt")
            } catch (r: Throwable) {
                r.printStackTrace()
            }
        }
    }
}