package com.study.lib.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.study.lib.common.R
import java.util.ArrayList

object PermissionUtil {
    private const val TAG = "PermissionUtil"
    fun isPermissionGranted(context: Context?, permission: String): Boolean {
        var result: Boolean
        val targetSdkVersion = SimpleUtil.getTargetSdkVersion(context!!)
        if (Manifest.permission.SYSTEM_ALERT_WINDOW == permission) {
            result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(context)
            } else {
                true
            }
        } else {
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can use Context#checkSelfPermission
                result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED
                //                LogUtil.i(TAG, "isPermissionGranted PermissionChecker.checkSelfPermission result[" + result + "]");
            }
        }
        Log.i(TAG, "isPermissionGranted permission[$permission] result[$result]")
        return result
    }

    fun getDeniedPermissions(context: Context?, vararg permissions: String): List<String> {
        val deniedList: MutableList<String> = ArrayList(1)
        for (permission in permissions) {
            if (!isPermissionGranted(context, permission)) {
                deniedList.add(permission)
            }
        }
        return deniedList
    }

    fun requestPermissions(activity: Activity?, permissions: Array<String?>?, requestCode: Int) {
        ActivityCompat.requestPermissions(activity!!, permissions!!, requestCode)
    }

    fun getRationalePermissions(context: Context, vararg permissions: String): List<String> {
        val rationaleList: MutableList<String> = ArrayList(1)
        for (permission in permissions) {
            if (isShowRationalePermission(context, permission)) {
                rationaleList.add(permission)
            }
        }
        return rationaleList
    }

    fun hasAlwaysDeniedPermission(context: Context, permissions: List<String>): Boolean {
        var result = false
        for (permission in permissions) {
            if (!isShowRationalePermission(context, permission)) {
                Log.i(TAG, "hasAlwaysDeniedPermission permission[$permission]")
                result = true
                break
            }
        }
        Log.i(TAG, "hasAlwaysDeniedPermission permissions[$permissions] result[$result]")
        return result
    }

    fun isShowRationalePermission(context: Context, permission: String?): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        if (context is Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale(context, permission!!)
        }
        val packageManager = context.packageManager
        val pkManagerClass: Class<*> = packageManager.javaClass
        return try {
            val method = pkManagerClass.getMethod("shouldShowRequestPermissionRationale", String::class.java)
            if (!method.isAccessible) method.isAccessible = true
            method.invoke(packageManager, permission) as Boolean
        } catch (ignored: Throwable) {
            false
        }
    }

    fun transformText(context: Context, permissions: List<String>): List<String> {
        val textList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            when (permission) {
                Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR -> {
                    val message = context.getString(R.string.permission_name_calendar)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.CAMERA -> {
                    val message = context.getString(R.string.permission_name_camera)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS -> {
                    val message = context.getString(R.string.permission_name_contacts)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    val message = context.getString(R.string.permission_name_location)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.RECORD_AUDIO -> {
                    val message = context.getString(R.string.permission_name_microphone)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.USE_SIP, Manifest.permission.PROCESS_OUTGOING_CALLS -> {
                    val message = context.getString(R.string.permission_name_phone)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.BODY_SENSORS -> {
                    val message = context.getString(R.string.permission_name_sensors)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS -> {
                    val message = context.getString(R.string.permission_name_sms)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    val message = context.getString(R.string.permission_name_storage)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                Manifest.permission.SYSTEM_ALERT_WINDOW -> {
                    val message = context.getString(R.string.permission_name_system_alert_window)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
            }
        }
        return textList
    }

    fun gotoSetOverLayPermission(context: Activity, requestCode: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.packageName))
                context.startActivityForResult(intent, requestCode)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    interface PermissionListener {
        fun beforeAlert() {}
        fun granted() {}
        fun denied(deniedList: List<String?>?) {}
        val requestPermissions: Array<String?>?
    }
}