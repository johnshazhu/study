package com.study.lib.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings

/**
 * 定位
 * Created by ZWT on 2015/8/6.
 */
class LocationUtil private constructor() {
    fun ReadLocation(context: Context?, locationListener: LocationListener?) {}

    /**
     * 检查GPS是否开启(备用)
     *
     * @return
     */
    fun isGPSEnabled(mContext: Context): Boolean {
        val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 打开gps设置界面(备用)
     */
    fun openGPSSetting(mContext: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            mContext.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {

            // The Android SDK doc says that the location settings activity
            // may not be found. In that case show the general settings.

            // General settings activity
            intent.action = Settings.ACTION_SETTINGS
            try {
                mContext.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    interface LocationListener {
        fun onReceiveLocation(city: String?, province: String?, latitude: Double, longitude: Double)
    }

    companion object {
        private var singleLocationUtil: LocationUtil? = null
        @JvmStatic
        val instance: LocationUtil?
            get() {
                if (singleLocationUtil == null) {
                    synchronized(LocationUtil::class.java) {
                        if (singleLocationUtil == null) {
                            singleLocationUtil = LocationUtil()
                        }
                    }
                }
                return singleLocationUtil
            }
    }
}