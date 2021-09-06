package com.study.lib.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.NetworkInterface
import java.util.*

open class SimpleUtil private constructor() {
    private var properties: Properties? = null
    private var localProperties: Properties? = null

    init {
        val context = InjectUtil.context()!!
        var inputStream: InputStream? = null
        var localInputStream: InputStream? = null
        try {
            inputStream = context.assets.open(CONFIG_FILENAME)
            properties = Properties()
            properties!!.load(inputStream)

            appId = properties!!.getProperty(APP_ID)
            val localFileName = getBaseDir(context) + LOCAL_CONFIG_FILENAME
            if (isExternalStorageReadable() && isFileExit(localFileName) &&
                    PermissionUtil.isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                localProperties = Properties()
                localProperties!!.load(FileInputStream(localFileName).also { localInputStream = it })
            } else {
                localProperties = null
            }
        } catch (r: Throwable) {
            r.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                localInputStream?.close()
            } catch (r: Throwable) {
                r.printStackTrace()
            }
        }
    }

    companion object {
        @JvmStatic
        val instance: SimpleUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SimpleUtil()
        }

        const val INVALID_INTEGER_VALUE = -1
        const val CONFIG_FILENAME = "app.properties"
        const val APP_ID = "appid"
        const val LOCAL_CONFIG_FILENAME = "app1.properties"
        lateinit var appId: String

        @JvmStatic
        fun getBaseDir(context: Context): String {
            val builder = StringBuilder()
            builder.append(Environment.getExternalStorageDirectory())
                    .append(File.separator)
                    .append(appId)
                    .append(File.separator)
            return builder.toString()
        }

        @JvmStatic
        fun getAppPropertiesPath(context: Context): String? {
            val cursor = context.contentResolver.query(MediaStore.Files.getContentUri("external"),
                    arrayOf(MediaStore.Files.FileColumns.DATA),
                    "${MediaStore.Files.FileColumns.DATA} like '%$appId/$LOCAL_CONFIG_FILENAME'",
                    null, null)
            cursor?.let {
                val dataIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                while (cursor.moveToNext()) {
                    val path = cursor.getString(dataIndex)
                    if (path != null) {
                        cursor.close()
                        return path
                    }
                }
            }

            return null
        }

        @JvmStatic
        fun getPathName() = LOCAL_CONFIG_FILENAME

        @JvmStatic
        fun getTargetSdkVersion(context: Context) = context.applicationInfo.targetSdkVersion

        @JvmStatic
        fun getPropertyValue(key: String): String {
            val value: String = instance.getValueByProperties(instance.localProperties, key)
            return if (value.isNotEmpty()) value else instance.getValueByProperties(instance.properties, key)
        }

        @JvmStatic
        fun getSignatureCode(context: Context): Int {
            var pi: PackageInfo? = null
            val pm = context.packageManager
            try {
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    PackageManager.GET_SIGNING_CERTIFICATES else
                    PackageManager.GET_SIGNATURES
                pi = pm.getPackageInfo(context.packageName, flag)
                if (pi == null) {
                    return 0
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (pi.signingInfo != null && pi.signingInfo.apkContentsSigners != null &&
                            pi.signingInfo.apkContentsSigners.isNotEmpty()) {
                        return pi.signingInfo.apkContentsSigners[0].hashCode()
                    }
                } else {
                    if (pi.signatures?.isNotEmpty() == true) {
                        return pi.signatures[0].hashCode()
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return 0
        }

        @JvmStatic
        fun getMacAddress(): String {
            var macAddress = ""
            val buf = StringBuffer()
            var networkInterface: NetworkInterface? = null
            try {
                networkInterface = NetworkInterface.getByName("eth1")
                if (networkInterface == null) {
                    networkInterface = NetworkInterface.getByName("wlan0")
                }
                if (networkInterface == null) {
                    return macAddress
                }
                val address = networkInterface.hardwareAddress
                for (b in address) {
                    buf.append(String.format("%02X:", b))
                }
                if (buf.isNotEmpty()) {
                    buf.deleteCharAt(buf.length - 1)
                }
                macAddress = buf.toString()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return macAddress
        }

        @JvmStatic
        fun trimString(value: String?): String {
            return value?.trim { it <= ' ' } ?: ""
        }

        @JvmStatic
        fun parseLong(value: String?): Long {
            return try {
                trimString(value).toLong()
            } catch (e: Exception) {
                0
            }
        }

        @JvmStatic
        fun getFormatString(format: String, vararg args: Any): String {
            return try {
                String.format(Locale.getDefault(), format, *args)
            } catch (e: UnknownFormatConversionException) {
                ""
            }
        }

        /**
         * 获取IMEI
         *
         * @param context
         * @return
         */
        @SuppressLint("MissingPermission")
        @JvmStatic
        fun getImei(context: Context): String? {
            var result: String? = null
            try {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                if (tm != null) {
                    result = tm.deviceId
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return result
        }

        /**
         * 获取android id
         *
         * @param context
         * @return
         */
        @SuppressLint("HardwareIds")
        @JvmStatic
        fun getAndroidId(context: Context): String? {
            var result: String? = null
            try {
                result = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return result
        }

        /**
         * 获取build serial
         *
         * @param context
         * @return
         */
        @SuppressLint("HardwareIds")
        @JvmStatic
        fun getBuildSerial(context: Context?): String? {
            var result: String? = null
            try {
                result = Build.SERIAL
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return result
        }

        @JvmStatic
        fun getFileExtensionFromUrl(url: String): String {
            var url = url
            if (!TextUtils.isEmpty(url)) {
                val fragment = url.lastIndexOf('#')
                if (fragment > 0) {
                    url = url.substring(0, fragment)
                }
                val query = url.lastIndexOf('?')
                if (query > 0) {
                    url = url.substring(0, query)
                }
                val filenamePos = url.lastIndexOf('/')
                val filename = if (0 <= filenamePos) url.substring(filenamePos + 1) else url

                // if the filename contains special characters, we don't
                // consider it valid for our matching purposes:
                if (!TextUtils.isEmpty(filename)) {
                    val dotPos = filename.lastIndexOf('.')
                    if (0 <= dotPos) {
                        return filename.substring(dotPos + 1)
                    }
                }
            }
            return ""
        }

        /**
         * 网络状态判断
         *
         * @param context
         * @return
         */
        @SuppressLint("MissingPermission")
        @JvmStatic
        fun hasNetwork(context: Context?): Boolean {
            try {
                if (null == context || null == context.applicationContext) {
                    return false
                }
                val manager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        ?: return false
                //java.lang.RuntimeException: android.os.DeadSystemException at android.net.ConnectivityManager.getActiveNetworkInfo(ConnectivityManager.java:758)
                val networkinfo = manager.activeNetworkInfo
                if (networkinfo == null || !networkinfo.isAvailable) {
                    return false
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return true
        }

        @JvmStatic
        fun getCount(list: List<*>?): Int {
            return list?.size ?: 0
        }

        /**
         * 返回网络信息
         *
         * @param context
         * @return
         */
        @SuppressLint("MissingPermission")
        @JvmStatic
        fun getNetType(context: Context): String? {
            return try {
                val connectivity = context
                        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val nwInfo = connectivity.activeNetworkInfo ?: return "unknow"
                nwInfo.toString()
            } catch (ex: java.lang.Exception) {
                "unknow"
            }
        }

        /**
         * 返回当前程序版本名
         */
        @JvmStatic
        fun getAppVersionName(context: Context): String? {
            return try {
                val pm = context.packageManager
                val pi = pm.getPackageInfo(context.packageName, 0)
                pi.versionName
            } catch (e: java.lang.Exception) {
                ""
            }
        }

        @JvmStatic
        fun getViewInfo(context: Context?, view: View): String {
            var result = view.javaClass.simpleName
            try {
                val id = view.id
                var name: String? = null
                if (View.NO_ID != id) {
                    name = context!!.resources.getResourceName(id)
                }
                if (TextUtils.isEmpty(name)) {
                    if (view is Button) {
                        result = view.text.toString()
                    } else if (view is TextView) {
                        result = view.text.toString()
                    } else {
                        val tag = view.tag
                        if (null != tag) {
                            result += " tag[$tag]"
                        }
                    }
                } else {
                    result = name
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return result
        }

        // Checks if a volume containing external storage is available to at least read.
        @JvmStatic
        fun isExternalStorageReadable(): Boolean {
            return Environment.getExternalStorageState() in
                    setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
        }

        // Checks if a volume containing external storage is available
        // for read and write.
        @JvmStatic
        fun isExternalStorageWritable(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

        @JvmStatic
        fun isFileExit(filename: String?): Boolean {
            return filename != null && filename.isNotEmpty() && File(filename).exists()
        }
    }

    fun getValueByProperties(data: Properties?, key: String): String {
        return data?.getProperty(key) ?: ""
    }
}