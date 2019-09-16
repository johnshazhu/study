package com.drcuiyutao.lib.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat

object NetworkUtil {
    fun hasNetwork(context: Context?): Boolean {
        try {
            if (context == null || context.applicationContext == null) {
                return false
            }
            val manager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
                val networkinfo = manager?.activeNetworkInfo
                if (networkinfo == null || !networkinfo.isAvailable) {
                    return false
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return true
    }
}
