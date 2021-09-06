package com.study.lib.util

import android.content.Context
import android.content.SharedPreferences

object SimpleSharedPreferencesUtil {
    private const val PREFERENCE_NAME = "qiniu_config"

    private val mPref: SharedPreferences?
    init {
        mPref = InjectUtil.context()?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun saveStringConfig(key: String, value: String) {
        if (mPref == null) {
            return
        }
        val editor = mPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveLongConfig(key: String, value: Long) {
        if (mPref == null) {
            return
        }
        val editor = mPref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getStringConfig(key: String): String {
        return mPref?.getString(key, "") ?: ""
    }

    fun getLongConfig(key: String, defValue: Long): Long {
        return mPref?.getLong(key, defValue) ?: defValue
    }

    fun clearInfor() {
        if (mPref == null) {
            return
        }
        val editor = mPref.edit()
        editor.clear()
        editor.apply()
    }
}