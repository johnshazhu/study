package com.study.lib.util

import android.content.Context

object UserUtil {
    @JvmStatic
    fun memberId(): String {
        return "0"
    }

    @JvmStatic
    fun identity(): String {
        return "0"
    }

    @JvmStatic
    fun isGuest(): Boolean {
        return false
    }

    @JvmStatic
    fun getIsVip(context: Context?): Boolean {
        return false
    }

    @JvmStatic
    fun isPregnant(): Boolean {
        return false
    }

    @JvmStatic
    fun isBoy(): Boolean {
        return false
    }

    @JvmStatic
    fun isGirl(): Boolean {
        return false
    }

    @JvmStatic
    fun getBabyBirthdayTimestamp(): Long {
        return 0
    }

    @JvmStatic
    fun getPregnantDays(): Int {
        return 0
    }

    @JvmStatic
    fun getBabyMonth(): Int {
        return 0
    }

    @JvmStatic
    fun getSex(): Int {
        return 0
    }

    @JvmStatic
    fun getAccountId(): String? {
        return null
    }

    @JvmStatic
    fun getCurChildId(): String? {
        return null
    }

    @JvmStatic
    fun userId(): Int {
        return 0
    }

    @JvmStatic
    fun token(): String {
        return ""
    }

    @JvmStatic
    fun babyBirthday(): String {
        return ""
    }

    @JvmStatic
    fun expectedDate(): String {
        return ""
    }

    @JvmStatic
    fun prematureOpen(): Boolean {
        return false
    }

    @JvmStatic
    fun getIsGestation(): Int {
        return 0
    }

    @JvmStatic
    fun getYxySkin(): Int {
        return 0
    }

    @JvmStatic
    fun getMemberId2(): String? {
        return null
    }

    @JvmStatic
    fun getLoginType(): Int {
        return -1
    }
}